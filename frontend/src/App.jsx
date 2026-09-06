import { useCallback, useEffect, useMemo, useState } from "react";
import {
  api,
  clearSession,
  isAuthenticated,
  saveSession,
  storedUser,
} from "./lib/api";

const navItems = [
  ["dashboard", "Overview", "⌂"],
  ["incidents", "Incidents", "◈"],
  ["shelters", "Shelters", "⌖"],
  ["requests", "Resource requests", "▤"],
  ["logs", "Incident logs", "≡"],
  ["report", "Report incident", "!"],
  ["admin", "Admin console", "⚙"],
];
const emptyIncident = {
  title: "",
  description: "",
  location: "",
  disasterType: "Flood",
  severity: "MEDIUM",
};
const emptyShelter = {
  name: "",
  address: "",
  city: "",
  capacity: 0,
  occupied: 0,
  latitude: "",
  longitude: "",
};
const errorMessage = (error) =>
  error instanceof Error ? error.message : "Something went wrong";
const formatDate = (value) =>
  value
    ? new Date(value).toLocaleString([], {
        dateStyle: "medium",
        timeStyle: "short",
      })
    : "—";
const initials = (user) =>
  (user?.fullName || user?.name || "User")
    .split(" ")
    .map((part) => part[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();

function App() {
  const [route, setRoute] = useState(
    window.location.hash.slice(1) || "dashboard",
  );
  const [user, setUser] = useState(storedUser());
  const [loading, setLoading] = useState(isAuthenticated());
  const [toast, setToast] = useState("");
  useEffect(() => {
    const onHash = () => setRoute(window.location.hash.slice(1) || "dashboard");
    window.addEventListener("hashchange", onHash);
    if (isAuthenticated())
      api
        .me()
        .then((profile) => setUser({ ...storedUser(), ...profile }))
        .catch(() => {
          clearSession();
          setUser(null);
        })
        .finally(() => setLoading(false));
    return () => window.removeEventListener("hashchange", onHash);
  }, []);
  const notify = useCallback((message) => {
    setToast(message);
    window.setTimeout(() => setToast(""), 3200);
  }, []);
  const navigate = (next) => {
    window.location.hash = next;
  };
  const signOut = () => {
    clearSession();
    setUser(null);
    navigate("login");
  };
  if (loading)
    return (
      <div className="loading-screen">Connecting to the response centre...</div>
    );
  if (!user || route === "login" || route === "register")
    return (
      <AuthPage
        mode={route === "register" ? "register" : "login"}
        onAuth={(auth) => {
          saveSession(auth);
          setUser(auth);
          navigate("dashboard");
        }}
      />
    );
  return (
    <div className="app-shell">
      <Sidebar
        route={route}
        navigate={navigate}
        user={user}
        signOut={signOut}
      />
      <main className="main-content">
        <Topbar route={route} user={user} signOut={signOut} />
        <div className="page-wrap">
          {route === "dashboard" && <Dashboard navigate={navigate} />}
          {route === "incidents" && (
            <Incidents navigate={navigate} notify={notify} />
          )}
          {route === "shelters" && <Shelters notify={notify} />}
          {route === "requests" && <Requests notify={notify} user={user} />}
          {route === "logs" && <Logs notify={notify} />}
          {route === "report" && (
            <ReportIncident navigate={navigate} notify={notify} />
          )}
          {route === "admin" && <Admin notify={notify} />}
        </div>
      </main>
      {toast && (
        <div className="toast">
          <span className="toast-dot" />
          {toast}
        </div>
      )}
    </div>
  );
}

function Sidebar({ route, navigate, user, signOut }) {
  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="brand-mark">+</div>
        <div>
          <strong>Haven</strong>
          <span>disaster response</span>
        </div>
      </div>
      <div className="workspace-label">OPERATIONS CENTRE</div>
      <nav>
        {navItems.map(([key, label, icon]) => (
          <button
            className={route === key ? "nav-item active" : "nav-item"}
            key={key}
            onClick={() => navigate(key)}
          >
            <span className="nav-icon">{icon}</span>
            {label}
          </button>
        ))}
      </nav>
      <div className="sidebar-footer">
        <div className="status-line">
          <span className="pulse" /> API connected
        </div>
        <button className="mini-user mini-user-button" onClick={signOut}>
          <div className="avatar">{initials(user)}</div>
          <div>
            <strong>{user.fullName || user.name}</strong>
            <span>
              {(user.role || user.roleName || "citizen").replace("_", " ")}
            </span>
          </div>
          <span className="dots">↪</span>
        </button>
      </div>
    </aside>
  );
}
function Topbar({ route, user, signOut }) {
  const titles = {
    dashboard: [
      "Operations overview",
      "Live data from the disaster response API.",
    ],
    incidents: [
      "Incident centre",
      "Monitor, filter, update, and delete disaster reports.",
    ],
    shelters: [
      "Emergency shelters",
      "Live capacity and availability across the network.",
    ],
    requests: [
      "Resource requests",
      "Track relief needs and update fulfilment status.",
    ],
    logs: [
      "Incident logs",
      "Record the operational timeline for each disaster.",
    ],
    report: [
      "Report an incident",
      "Share verified details so response teams can act quickly.",
    ],
    admin: ["Admin console", "Manage users, roles, and platform operations."],
  };
  const [title, subtitle] = titles[route] || titles.dashboard;
  return (
    <header className="topbar">
      <div>
        <div className="eyebrow">DISASTER RESPONSE PLATFORM</div>
        <h1>{title}</h1>
        <p>{subtitle}</p>
      </div>
      <div className="top-actions">
        <span className="api-badge">
          <i /> LIVE API
        </span>
        <button className="profile-button" onClick={signOut}>
          <div className="avatar">{initials(user)}</div>
          <span>{user.fullName || user.name}</span>
          <b>↪</b>
        </button>
      </div>
    </header>
  );
}
function Loading() {
  return <div className="panel loading-panel">Loading live data...</div>;
}
function ErrorPanel({ message, onRetry }) {
  return (
    <div className="panel error-panel">
      <strong>Could not load this view</strong>
      <p>{message}</p>
      {onRetry && (
        <button className="secondary-button" onClick={onRetry}>
          Try again
        </button>
      )}
    </div>
  );
}
function Status({ value }) {
  return (
    <span
      className={`status status-${String(value || "unknown").toLowerCase()}`}
    >
      {String(value || "UNKNOWN").replaceAll("_", " ")}
    </span>
  );
}
function Stat({ label, value, trend, accent }) {
  return (
    <div className="stat-card">
      <div className={`stat-accent ${accent}`} />
      <span>{label}</span>
      <strong>{value}</strong>
      <small>{trend}</small>
    </div>
  );
}
function PanelHead({ title, action, onAction }) {
  return (
    <div className="panel-head">
      <h3>{title}</h3>
      {action && (
        <button className="text-button" onClick={onAction}>
          {action} <span>→</span>
        </button>
      )}
    </div>
  );
}
function Empty({ text }) {
  return <div className="empty-state">{text}</div>;
}
function Field({ label, children, wide }) {
  return (
    <label className={wide ? "wide" : ""}>
      {label}
      {children}
    </label>
  );
}
function FormCard({
  title,
  description,
  children,
  onCancel,
  onSubmit,
  submitText = "Save changes",
}) {
  return (
    <section className="form-card">
      <div className="form-title">
        <span className="tag blue">RESPONSE CENTRE</span>
        <h2>{title}</h2>
        {description && <p>{description}</p>}
      </div>
      <form onSubmit={onSubmit}>
        {children}
        <div className="form-actions">
          <button type="button" className="secondary-button" onClick={onCancel}>
            Cancel
          </button>
          <button className="primary-button">{submitText} →</button>
        </div>
      </form>
    </section>
  );
}

function Dashboard({ navigate }) {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const load = () =>
    Promise.all([api.disasters(), api.shelters(), api.requests()])
      .then(([disasters, shelters, requests]) =>
        setData({ disasters, shelters, requests }),
      )
      .catch((e) => setError(errorMessage(e)));
  useEffect(load, []);
  if (error)
    return (
      <ErrorPanel
        message={error}
        onRetry={() => {
          setError("");
          load();
        }}
      />
    );
  if (!data) return <Loading />;
  const active = data.disasters.filter((item) =>
    ["HIGH", "CRITICAL"].includes(item.severity),
  ).length;
  const occupied = data.shelters.reduce(
    (sum, item) => sum + (item.occupied || 0),
    0,
  );
  const beds = data.shelters.reduce(
    (sum, item) => sum + (item.capacity || 0),
    0,
  );
  const open = data.requests.filter((item) => item.status === "PENDING").length;
  return (
    <>
      <section className="hero-banner">
        <div>
          <span className="tag orange">LIVE OPERATIONS</span>
          <h2>Clarity when every second counts.</h2>
          <p>
            One command view for incidents, shelters, and relief moving through
            your network.
          </p>
          <button className="primary-button" onClick={() => navigate("report")}>
            Report an incident <span>↗</span>
          </button>
        </div>
        <div className="hero-radar">
          <div className="radar-ring ring-one" />
          <div className="radar-ring ring-two" />
          <div className="radar-sweep" />
          <strong>{String(active).padStart(2, "0")}</strong>
          <small>
            HIGH PRIORITY
            <br />
            SIGNALS
          </small>
        </div>
      </section>
      <div className="stat-grid">
        <Stat
          label="High priority incidents"
          value={active}
          trend={`${data.disasters.length} total reports`}
          accent="red"
        />
        <Stat
          label="People in shelters"
          value={occupied}
          trend={`${beds ? Math.round((occupied / beds) * 100) : 0}% network occupancy`}
          accent="yellow"
        />
        <Stat
          label="Open requests"
          value={open}
          trend={`${data.requests.length} total requests`}
          accent="blue"
        />
        <Stat
          label="Shelters online"
          value={data.shelters.length}
          trend="Live capacity data"
          accent="green"
        />
      </div>
      <div className="dashboard-grid">
        <section className="panel">
          <PanelHead
            title="Latest incidents"
            action="View all"
            onAction={() => navigate("incidents")}
          />
          <div className="incident-list">
            {data.disasters.slice(0, 4).map((item) => (
              <IncidentRow key={item.id} incident={item} />
            ))}
          </div>
        </section>
        <section className="panel">
          <PanelHead
            title="Shelter capacity"
            action="Explore shelters"
            onAction={() => navigate("shelters")}
          />
          {data.shelters.slice(0, 4).map((shelter) => (
            <CapacityRow key={shelter.id} shelter={shelter} />
          ))}
        </section>
      </div>
      <section className="panel activity-panel">
        <PanelHead
          title="Latest resource requests"
          action="Open requests"
          onAction={() => navigate("requests")}
        />
        {data.requests.slice(0, 5).map((item) => (
          <div className="activity-row" key={item.id}>
            <div className="activity-icon">↗</div>
            <div>
              <strong>
                {item.resourceType} request <span>#{item.id}</span>
              </strong>
              <p>
                {item.disasterTitle || `Disaster #${item.disasterReportId}`} ·{" "}
                {item.quantity} units
              </p>
            </div>
            <Status value={item.status} />
          </div>
        ))}
        {!data.requests.length && <Empty text="No resource requests yet." />}
      </section>
    </>
  );
}
function IncidentRow({ incident, onSelect }) {
  return (
    <button className="incident-button" onClick={onSelect}>
      <div className="incident-row">
        <div
          className={`severity-marker ${String(incident.severity).toLowerCase()}`}
        />
        <div className="incident-main">
          <div>
            <strong>{incident.title}</strong>
            <span className="location">⌖ {incident.location}</span>
          </div>
          <p>{incident.description || "No description provided."}</p>
          <small>{formatDate(incident.reportedAt)}</small>
        </div>
        <Status value={incident.severity} />
      </div>
    </button>
  );
}
function CapacityRow({ shelter }) {
  const percent = shelter.capacity
    ? Math.round((shelter.occupied / shelter.capacity) * 100)
    : 0;
  return (
    <div className="capacity-row">
      <span>
        {shelter.name}
        <small>{shelter.city}</small>
        <div className="progress">
          <span style={{ width: `${Math.min(percent, 100)}%` }} />
        </div>
      </span>
      <strong>
        {shelter.availableCapacity ??
          Math.max((shelter.capacity || 0) - (shelter.occupied || 0), 0)}{" "}
        free
      </strong>
    </div>
  );
}

function Incidents({ navigate, notify }) {
  const [items, setItems] = useState(null);
  const [query, setQuery] = useState("");
  const [severity, setSeverity] = useState("ALL");
  const [selected, setSelected] = useState(null);
  const [error, setError] = useState("");
  const load = () =>
    api
      .disasters()
      .then(setItems)
      .catch((e) => setError(errorMessage(e)));
  useEffect(load, []);
  const visible = useMemo(
    () =>
      (items || []).filter(
        (item) =>
          (severity === "ALL" || item.severity === severity) &&
          `${item.title} ${item.location}`
            .toLowerCase()
            .includes(query.toLowerCase()),
      ),
    [items, query, severity],
  );
  const remove = async (id) => {
    if (!window.confirm("Delete this disaster report?")) return;
    try {
      await api.deleteDisaster(id);
      notify("Incident deleted");
      setSelected(null);
      load();
    } catch (e) {
      notify(errorMessage(e));
    }
  };
  if (error)
    return (
      <ErrorPanel
        message={error}
        onRetry={() => {
          setError("");
          load();
        }}
      />
    );
  if (!items) return <Loading />;
  return (
    <>
      <div className="toolbar">
        <div className="search">
          <span>⌕</span>
          <input
            placeholder="Search location or incident"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>
        <select value={severity} onChange={(e) => setSeverity(e.target.value)}>
          <option>ALL</option>
          <option>CRITICAL</option>
          <option>HIGH</option>
          <option>MEDIUM</option>
          <option>LOW</option>
        </select>
        <button className="primary-button" onClick={() => navigate("report")}>
          + Report incident
        </button>
      </div>
      <div className="content-grid">
        <section className="panel">
          <PanelHead title={`${visible.length} incidents in view`} />
          <div className="incident-list full-list">
            {visible.map((item) => (
              <IncidentRow
                key={item.id}
                incident={item}
                onSelect={() => setSelected(item)}
              />
            ))}
            {!visible.length && (
              <Empty text="No incidents match your filters." />
            )}
          </div>
        </section>
        {selected && (
          <IncidentDetail
            incident={selected}
            onClose={() => setSelected(null)}
            onDelete={() => remove(selected.id)}
            notify={notify}
          />
        )}
      </div>
    </>
  );
}
function IncidentDetail({ incident, onClose, onDelete, notify }) {
  const [form, setForm] = useState({
    title: incident.title,
    description: incident.description || "",
    location: incident.location,
    disasterType: incident.disasterType,
    severity: incident.severity,
  });
  const [logs, setLogs] = useState([]);
  const [message, setMessage] = useState("");
  useEffect(() => {
    api
      .logs(incident.id)
      .then(setLogs)
      .catch(() => {});
  }, [incident.id]);
  const update = async (e) => {
    e.preventDefault();
    try {
      await api.updateDisaster(incident.id, form);
      notify("Incident updated");
      onClose();
    } catch (err) {
      notify(errorMessage(err));
    }
  };
  const addLog = async (e) => {
    e.preventDefault();
    try {
      const created = await api.createLog({
        message,
        disasterReportId: incident.id,
      });
      setLogs((current) => [created, ...current]);
      setMessage("");
      notify("Incident log added");
    } catch (err) {
      notify(errorMessage(err));
    }
  };
  return (
    <aside className="panel detail-panel">
      <div className="panel-head">
        <h3>Incident #{incident.id}</h3>
        <button className="more-button" onClick={onClose}>
          ×
        </button>
      </div>
      <form onSubmit={update} className="stack-form">
        <Field label="Title">
          <input
            value={form.title}
            onChange={(e) => setForm({ ...form, title: e.target.value })}
            required
          />
        </Field>
        <Field label="Location">
          <input
            value={form.location}
            onChange={(e) => setForm({ ...form, location: e.target.value })}
            required
          />
        </Field>
        <div className="form-grid two">
          <Field label="Type">
            <input
              value={form.disasterType || ""}
              onChange={(e) =>
                setForm({ ...form, disasterType: e.target.value })
              }
              required
            />
          </Field>
          <Field label="Severity">
            <select
              value={form.severity}
              onChange={(e) => setForm({ ...form, severity: e.target.value })}
            >
              <option>LOW</option>
              <option>MEDIUM</option>
              <option>HIGH</option>
              <option>CRITICAL</option>
            </select>
          </Field>
        </div>
        <Field label="Description">
          <textarea
            rows="4"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
          />
        </Field>
        <div className="form-actions">
          <button
            className="secondary-button danger-button"
            type="button"
            onClick={onDelete}
          >
            Delete
          </button>
          <button className="primary-button">Save changes</button>
        </div>
      </form>
      <div className="detail-logs">
        <h4>Operational timeline</h4>
        {logs.map((log) => (
          <div className="log-item" key={log.id}>
            <strong>{log.message}</strong>
            <small>{formatDate(log.createdAt)}</small>
          </div>
        ))}
        <form onSubmit={addLog} className="inline-form">
          <input
            placeholder="Add a timeline update"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            required
          />
          <button className="secondary-button">Add</button>
        </form>
      </div>
    </aside>
  );
}

function Shelters({ notify }) {
  const [items, setItems] = useState(null);
  const [city, setCity] = useState("");
  const [form, setForm] = useState(null);
  const [error, setError] = useState("");
  const load = () =>
    api
      .shelters()
      .then(setItems)
      .catch((e) => setError(errorMessage(e)));
  useEffect(load, []);
  if (error)
    return (
      <ErrorPanel
        message={error}
        onRetry={() => {
          setError("");
          load();
        }}
      />
    );
  if (!items) return <Loading />;
  const visible = items.filter((s) =>
    `${s.name} ${s.city}`.toLowerCase().includes(city.toLowerCase()),
  );
  const save = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        ...form,
        capacity: Number(form.capacity),
        occupied: Number(form.occupied),
        latitude: form.latitude ? Number(form.latitude) : null,
        longitude: form.longitude ? Number(form.longitude) : null,
      };
      if (form.id) await api.updateShelter(form.id, payload);
      else await api.createShelter(payload);
      notify(form.id ? "Shelter updated" : "Shelter registered");
      setForm(null);
      load();
    } catch (err) {
      notify(errorMessage(err));
    }
  };
  const remove = async (id) => {
    if (!window.confirm("Delete this shelter?")) return;
    try {
      await api.deleteShelter(id);
      notify("Shelter deleted");
      load();
    } catch (e) {
      notify(errorMessage(e));
    }
  };
  return (
    <>
      {form ? (
        <FormCard
          title={form.id ? "Edit shelter" : "Register shelter"}
          onCancel={() => setForm(null)}
          onSubmit={save}
        >
          <div className="form-grid two">
            <Field label="Name">
              <input
                value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })}
                required
              />
            </Field>
            <Field label="City">
              <input
                value={form.city}
                onChange={(e) => setForm({ ...form, city: e.target.value })}
                required
              />
            </Field>
            <Field label="Address" wide>
              <input
                value={form.address}
                onChange={(e) => setForm({ ...form, address: e.target.value })}
                required
              />
            </Field>
            <Field label="Capacity">
              <input
                type="number"
                min="0"
                value={form.capacity}
                onChange={(e) => setForm({ ...form, capacity: e.target.value })}
                required
              />
            </Field>
            <Field label="Occupied">
              <input
                type="number"
                min="0"
                value={form.occupied}
                onChange={(e) => setForm({ ...form, occupied: e.target.value })}
                required
              />
            </Field>
          </div>
        </FormCard>
      ) : (
        <>
          <div className="toolbar">
            <div className="search">
              <span>⌕</span>
              <input
                placeholder="Search city or shelter"
                value={city}
                onChange={(e) => setCity(e.target.value)}
              />
            </div>
            <button
              className="primary-button"
              onClick={() => setForm({ ...emptyShelter })}
            >
              + Register shelter
            </button>
          </div>
          <div className="shelter-grid">
            {visible.map((shelter) => (
              <article className="shelter-card" key={shelter.id}>
                <div className="shelter-top">
                  <span className="shelter-icon">⌂</span>
                  <Status
                    value={shelter.availableCapacity > 0 ? "OPEN" : "FULL"}
                  />
                </div>
                <h3>{shelter.name}</h3>
                <p className="muted">
                  ⌖ {shelter.address}, {shelter.city}
                </p>
                <div className="beds">
                  <div>
                    <strong>{shelter.availableCapacity}</strong>
                    <span>beds available</span>
                  </div>
                  <div className="bed-total">
                    {shelter.occupied} / {shelter.capacity}
                  </div>
                </div>
                <div className="progress">
                  <span
                    style={{
                      width: `${shelter.capacity ? (shelter.occupied / shelter.capacity) * 100 : 0}%`,
                    }}
                  />
                </div>
                <div className="shelter-footer">
                  <button
                    className="text-button"
                    onClick={() => setForm({ ...shelter })}
                  >
                    Edit
                  </button>
                  <button
                    className="text-button danger-text"
                    onClick={() => remove(shelter.id)}
                  >
                    Delete
                  </button>
                </div>
              </article>
            ))}
            {!visible.length && <Empty text="No shelters found." />}
          </div>
        </>
      )}
    </>
  );
}

function Requests({ notify, user }) {
  const [items, setItems] = useState(null);
  const [disasters, setDisasters] = useState([]);
  const [status, setStatus] = useState("ALL");
  const [form, setForm] = useState(null);
  const [error, setError] = useState("");
  const load = () =>
    Promise.all([api.requests(), api.disasters()])
      .then(([requests, reports]) => {
        setItems(requests);
        setDisasters(reports);
      })
      .catch((e) => setError(errorMessage(e)));
  useEffect(load, []);
  if (error)
    return (
      <ErrorPanel
        message={error}
        onRetry={() => {
          setError("");
          load();
        }}
      />
    );
  if (!items) return <Loading />;
  const visible = items.filter(
    (item) => status === "ALL" || item.status === status,
  );
  const save = async (e) => {
    e.preventDefault();
    try {
      await api.createRequest({
        resourceType: form.resourceType,
        quantity: Number(form.quantity),
        userId: Number(form.userId || user.userId),
        disasterReportId: Number(form.disasterReportId),
      });
      notify("Resource request submitted");
      setForm(null);
      load();
    } catch (err) {
      notify(errorMessage(err));
    }
  };
  const updateStatus = async (id, value) => {
    try {
      await api.updateRequestStatus(id, value);
      notify("Request status updated");
      load();
    } catch (e) {
      notify(errorMessage(e));
    }
  };
  return (
    <>
      {form ? (
        <FormCard
          title="New resource request"
          onCancel={() => setForm(null)}
          onSubmit={save}
        >
          <div className="form-grid two">
            <Field label="Resource">
              <select
                value={form.resourceType}
                onChange={(e) =>
                  setForm({ ...form, resourceType: e.target.value })
                }
              >
                <option>WATER</option>
                <option>FOOD</option>
                <option>MEDICINE</option>
                <option>CLOTHES</option>
                <option>RESCUE_EQUIPMENT</option>
              </select>
            </Field>
            <Field label="Quantity">
              <input
                type="number"
                min="1"
                value={form.quantity}
                onChange={(e) => setForm({ ...form, quantity: e.target.value })}
                required
              />
            </Field>
            <Field label="Disaster report" wide>
              <select
                value={form.disasterReportId}
                onChange={(e) =>
                  setForm({ ...form, disasterReportId: e.target.value })
                }
                required
              >
                <option value="">Choose an incident</option>
                {disasters.map((d) => (
                  <option key={d.id} value={d.id}>
                    #{d.id} · {d.title}
                  </option>
                ))}
              </select>
            </Field>
          </div>
        </FormCard>
      ) : (
        <>
          <div className="toolbar">
            <div className="filter-tabs">
              {[
                "ALL",
                "PENDING",
                "APPROVED",
                "IN_PROGRESS",
                "DELIVERED",
                "REJECTED",
              ].map((item) => (
                <button
                  className={status === item ? "selected" : ""}
                  key={item}
                  onClick={() => setStatus(item)}
                >
                  {item.replace("_", " ")}
                </button>
              ))}
            </div>
            <button
              className="primary-button"
              onClick={() =>
                setForm({
                  resourceType: "WATER",
                  quantity: 1,
                  userId: user.userId,
                  disasterReportId: "",
                })
              }
            >
              + New request
            </button>
          </div>
          <section className="panel table-panel">
            <table>
              <thead>
                <tr>
                  <th>Request</th>
                  <th>Resource</th>
                  <th>Incident</th>
                  <th>Requester</th>
                  <th>Status</th>
                  <th>Update</th>
                </tr>
              </thead>
              <tbody>
                {visible.map((item) => (
                  <tr key={item.id}>
                    <td>
                      <strong>#{item.id}</strong>
                      <small>{formatDate(item.requestedAt)}</small>
                    </td>
                    <td>
                      <span className="resource-label">
                        {item.resourceType}
                      </span>
                      <small>{item.quantity} units</small>
                    </td>
                    <td>{item.disasterTitle || `#${item.disasterReportId}`}</td>
                    <td>{item.userName || `User #${item.userId}`}</td>
                    <td>
                      <Status value={item.status} />
                    </td>
                    <td>
                      <select
                        value={item.status}
                        onChange={(e) => updateStatus(item.id, e.target.value)}
                      >
                        <option>PENDING</option>
                        <option>APPROVED</option>
                        <option>IN_PROGRESS</option>
                        <option>DELIVERED</option>
                        <option>REJECTED</option>
                      </select>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {!visible.length && <Empty text="No requests match this status." />}
          </section>
        </>
      )}
    </>
  );
}

function Logs({ notify }) {
  const [disasters, setDisasters] = useState([]);
  const [selected, setSelected] = useState("");
  const [logs, setLogs] = useState([]);
  const [message, setMessage] = useState("");
  useEffect(() => {
    api
      .disasters()
      .then(setDisasters)
      .catch((e) => notify(errorMessage(e)));
  }, [notify]);
  useEffect(() => {
    if (selected)
      api
        .logs(selected)
        .then(setLogs)
        .catch((e) => notify(errorMessage(e)));
  }, [selected, notify]);
  const add = async (e) => {
    e.preventDefault();
    try {
      const created = await api.createLog({
        message,
        disasterReportId: Number(selected),
      });
      setLogs((current) => [created, ...current]);
      setMessage("");
      notify("Log entry recorded");
    } catch (err) {
      notify(errorMessage(err));
    }
  };
  const remove = async (id) => {
    try {
      await api.deleteLog(id);
      setLogs((current) => current.filter((item) => item.id !== id));
      notify("Log entry deleted");
    } catch (e) {
      notify(errorMessage(e));
    }
  };
  return (
    <section className="panel logs-panel">
      <div className="toolbar compact">
        <select value={selected} onChange={(e) => setSelected(e.target.value)}>
          <option value="">Choose an incident</option>
          {disasters.map((d) => (
            <option key={d.id} value={d.id}>
              #{d.id} · {d.title}
            </option>
          ))}
        </select>
      </div>
      {selected ? (
        <>
          <form onSubmit={add} className="inline-form log-form">
            <input
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              placeholder="Describe an operational update"
              required
            />
            <button className="primary-button">Record update</button>
          </form>
          <div className="timeline">
            {logs.map((log) => (
              <div className="timeline-item" key={log.id}>
                <span className="timeline-dot" />
                <div>
                  <strong>{log.message}</strong>
                  <small>{formatDate(log.createdAt)}</small>
                </div>
                <button className="more-button" onClick={() => remove(log.id)}>
                  ×
                </button>
              </div>
            ))}
            {!logs.length && (
              <Empty text="No operational updates for this incident." />
            )}
          </div>
        </>
      ) : (
        <Empty text="Select an incident to view its timeline." />
      )}
    </section>
  );
}

function ReportIncident({ navigate, notify }) {
  const [form, setForm] = useState(emptyIncident);
  const [saving, setSaving] = useState(false);
  const submit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await api.createDisaster({
        ...form,
        reportedAt: new Date().toISOString(),
      });
      notify("Incident report submitted");
      navigate("incidents");
    } catch (err) {
      notify(errorMessage(err));
    } finally {
      setSaving(false);
    }
  };
  return (
    <FormCard
      title="Report an incident"
      description="Provide enough detail for response teams to act."
      onCancel={() => navigate("dashboard")}
      onSubmit={submit}
      submitText={saving ? "Submitting..." : "Submit report"}
    >
      <div className="form-grid two">
        <Field label="Incident title" wide>
          <input
            value={form.title}
            onChange={(e) => setForm({ ...form, title: e.target.value })}
            placeholder="What is happening?"
            required
          />
        </Field>
        <Field label="Incident type">
          <input
            value={form.disasterType}
            onChange={(e) => setForm({ ...form, disasterType: e.target.value })}
            required
          />
        </Field>
        <Field label="Severity">
          <select
            value={form.severity}
            onChange={(e) => setForm({ ...form, severity: e.target.value })}
          >
            <option>LOW</option>
            <option>MEDIUM</option>
            <option>HIGH</option>
            <option>CRITICAL</option>
          </select>
        </Field>
        <Field label="Location" wide>
          <input
            value={form.location}
            onChange={(e) => setForm({ ...form, location: e.target.value })}
            placeholder="City, neighbourhood, or landmark"
            required
          />
        </Field>
        <Field label="Description" wide>
          <textarea
            rows="6"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            placeholder="Describe the situation and immediate needs"
          />
        </Field>
      </div>
    </FormCard>
  );
}

function Admin({ notify }) {
  const [users, setUsers] = useState(null);
  const [form, setForm] = useState(null);
  const load = () =>
    api
      .users()
      .then(setUsers)
      .catch((e) => notify(errorMessage(e)));
  useEffect(() => {
    api.users().then(setUsers).catch((e) => notify(errorMessage(e)));
  }, [notify]);
  if (!users) return <Loading />;
  const save = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        fullName: form.fullName,
        email: form.email,
        phone: form.phone,
        city: form.city,
        role: form.roleName,
      };
      if (form.id) await api.updateUser(form.id, payload);
      else await api.createUser({ ...payload, password: form.password });
      notify(form.id ? "User updated" : "User created");
      setForm(null);
      load();
    } catch (err) {
      notify(errorMessage(err));
    }
  };
  const remove = async (id) => {
    if (!window.confirm("Delete this user?")) return;
    try {
      await api.deleteUser(id);
      notify("User deleted");
      load();
    } catch (e) {
      notify(errorMessage(e));
    }
  };
  return (
    <>
      {form ? (
        <FormCard
          title={form.id ? "Edit user" : "Create user"}
          onCancel={() => setForm(null)}
          onSubmit={save}
        >
          <div className="form-grid two">
            <Field label="Full name">
              <input
                value={form.fullName}
                onChange={(e) => setForm({ ...form, fullName: e.target.value })}
                required
              />
            </Field>
            <Field label="Email">
              <input
                type="email"
                value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })}
                required
              />
            </Field>
            <Field label="Phone">
              <input
                value={form.phone || ""}
                onChange={(e) => setForm({ ...form, phone: e.target.value })}
                required
              />
            </Field>
            <Field label="City">
              <input
                value={form.city || ""}
                onChange={(e) => setForm({ ...form, city: e.target.value })}
                required
              />
            </Field>
            <Field label="Role">
              <select
                value={form.roleName}
                onChange={(e) => setForm({ ...form, roleName: e.target.value })}
              >
                <option>CITIZEN</option>
                <option>VOLUNTEER</option>
                <option>RESCUE_TEAM</option>
                <option>ADMIN</option>
              </select>
            </Field>
            {!form.id && (
              <Field label="Password">
                <input
                  type="password"
                  value={form.password || ""}
                  onChange={(e) =>
                    setForm({ ...form, password: e.target.value })
                  }
                  required
                />
              </Field>
            )}
          </div>
        </FormCard>
      ) : (
        <>
          <div className="admin-banner">
            <div>
              <span className="tag red">ADMIN ONLY</span>
              <h2>People power the response.</h2>
              <p>Keep roles and access in order.</p>
            </div>
            <button
              className="secondary-button"
              onClick={() =>
                setForm({
                  fullName: "",
                  email: "",
                  phone: "",
                  city: "",
                  roleName: "CITIZEN",
                  password: "",
                })
              }
            >
              + Add user
            </button>
          </div>
          <section className="panel table-panel">
            <PanelHead title={`${users.length} registered users`} />
            <table>
              <thead>
                <tr>
                  <th>User</th>
                  <th>Role</th>
                  <th>City</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map((item) => (
                  <tr key={item.id}>
                    <td>
                      <div className="user-cell">
                        <div className="avatar small">{initials(item)}</div>
                        <strong>{item.fullName}</strong>
                      </div>
                      <small>{item.email}</small>
                    </td>
                    <td>
                      <span className="role-label">
                        {String(item.roleName).replace("_", " ")}
                      </span>
                    </td>
                    <td>{item.city}</td>
                    <td>
                      <Status value={item.enabled ? "ACTIVE" : "DISABLED"} />
                    </td>
                    <td>
                      <button
                        className="text-button"
                        onClick={() => setForm({ ...item })}
                      >
                        Edit
                      </button>
                      <button
                        className="text-button danger-text"
                        onClick={() => remove(item.id)}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </section>
        </>
      )}
    </>
  );
}

function AuthPage({ mode, onAuth }) {
  const register = mode === "register";
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    password: "",
    phone: "",
    city: "",
    role: "CITIZEN",
  });
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  const update = (key, value) => setForm({ ...form, [key]: value });
  const submit = async (e) => {
    e.preventDefault();
    setError("");
    setBusy(true);
    try {
      const auth = register
        ? await api.register(form)
        : await api.login({ email: form.email, password: form.password });
      onAuth(auth);
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setBusy(false);
    }
  };
  return (
    <div className="auth-page">
      <div className="auth-visual">
        <div className="brand light">
          <div className="brand-mark">+</div>
          <div>
            <strong>Haven</strong>
            <span>disaster response</span>
          </div>
        </div>
        <div className="auth-copy">
          <span className="tag orange">COORDINATED RESPONSE</span>
          <h1>Make the next right action easier.</h1>
          <p>
            A shared operating picture for communities, volunteers, and teams.
          </p>
        </div>
        <div className="auth-quote">
          Preparedness turns uncertainty into options.
        </div>
      </div>
      <div className="auth-panel">
        <div className="auth-form">
          <span className="tag blue">HAVEN PLATFORM</span>
          <h2>{register ? "Create your account" : "Welcome back"}</h2>
          <p>
            {register
              ? "Join the response network in your community."
              : "Sign in to your operations workspace."}
          </p>
          {error && <div className="form-error">{error}</div>}
          <form onSubmit={submit}>
            {register && (
              <>
                <Field label="Full name">
                  <input
                    value={form.fullName}
                    onChange={(e) => update("fullName", e.target.value)}
                    required
                  />
                </Field>
                <Field label="Phone">
                  <input
                    value={form.phone}
                    onChange={(e) => update("phone", e.target.value)}
                    required
                  />
                </Field>
                <Field label="City">
                  <input
                    value={form.city}
                    onChange={(e) => update("city", e.target.value)}
                    required
                  />
                </Field>
              </>
            )}
            <Field label="Email address">
              <input
                type="email"
                value={form.email}
                onChange={(e) => update("email", e.target.value)}
                required
              />
            </Field>
            <Field label="Password">
              <input
                type="password"
                minLength="6"
                value={form.password}
                onChange={(e) => update("password", e.target.value)}
                required
              />
            </Field>
            {register && (
              <Field label="Join as">
                <select
                  value={form.role}
                  onChange={(e) => update("role", e.target.value)}
                >
                  <option>CITIZEN</option>
                  <option>VOLUNTEER</option>
                  <option>RESCUE_TEAM</option>
                </select>
              </Field>
            )}
            <button className="primary-button full" disabled={busy}>
              {busy
                ? "Connecting..."
                : register
                  ? "Create account →"
                  : "Sign in →"}
            </button>
          </form>
          <p className="switch-auth">
            {register ? "Already have an account?" : "New to Haven?"}{" "}
            <button
              onClick={() => {
                window.location.hash = register ? "login" : "register";
              }}
            >
              {register ? "Sign in" : "Create an account"}
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}

export default App;
