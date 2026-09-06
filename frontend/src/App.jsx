import { useEffect, useState } from "react";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/v1";

async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(localStorage.getItem("haven_token")
        ? { Authorization: `Bearer ${localStorage.getItem("haven_token")}` }
        : {}),
      ...options.headers,
    },
  });
  const body = await response.json().catch(() => ({}));
  if (!response.ok || body.success === false) {
    throw new Error(body.message || `Request failed (${response.status})`);
  }
  return body.data;
}

const api = {
  login: (payload) =>
    apiRequest("/auth/login", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
  register: (payload) =>
    apiRequest("/auth/register", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
  me: () => apiRequest("/auth/me"),
  disasters: () => apiRequest("/disasters/all"),
  createDisaster: (payload) =>
    apiRequest("/disasters", { method: "POST", body: JSON.stringify(payload) }),
  shelters: () => apiRequest("/shelters/all"),
  createShelter: (payload) =>
    apiRequest("/shelters", { method: "POST", body: JSON.stringify(payload) }),
  requests: () => apiRequest("/resource-requests/all"),
  createRequest: (payload) =>
    apiRequest("/resource-requests", {
      method: "POST",
      body: JSON.stringify(payload),
    }),
  updateRequestStatus: (id, status) =>
    apiRequest(`/resource-requests/${id}/status`, {
      method: "PUT",
      body: JSON.stringify({ status }),
    }),
  users: () => apiRequest("/users/all"),
};

const seedIncidents = [
  {
    id: 1042,
    title: "River Kaveri flooding",
    location: "Mysuru, Karnataka",
    severity: "HIGH",
    status: "VERIFIED",
    updated: "12 min ago",
    description:
      "Water levels are rising near low-lying neighborhoods. Rescue teams are coordinating evacuations.",
  },
  {
    id: 1041,
    title: "Urban fire reported",
    location: "Indiranagar, Bengaluru",
    severity: "MEDIUM",
    status: "IN_REVIEW",
    updated: "38 min ago",
    description:
      "A commercial building fire has been contained. Medical assistance is on site.",
  },
  {
    id: 1039,
    title: "Landslide on NH 48",
    location: "Sakleshpur, Karnataka",
    severity: "CRITICAL",
    status: "ACTIVE",
    updated: "1 hr ago",
    description:
      "Traffic is blocked in both directions. Heavy equipment has been dispatched.",
  },
  {
    id: 1038,
    title: "Storm damage assessment",
    location: "Udupi, Karnataka",
    severity: "LOW",
    status: "RESOLVED",
    updated: "3 hrs ago",
    description: "Power restoration and debris clearing are complete.",
  },
];

const seedShelters = [
  {
    name: "Jayanagar Community Hall",
    city: "Bengaluru",
    address: "4th Block, Jayanagar",
    capacity: 420,
    occupied: 286,
    status: "OPEN",
    phone: "+91 80 2456 9088",
  },
  {
    name: "Mysuru District Relief Centre",
    city: "Mysuru",
    address: "Hunsur Road, near DC Office",
    capacity: 650,
    occupied: 512,
    status: "OPEN",
    phone: "+91 821 242 1000",
  },
  {
    name: "St. Marys School",
    city: "Udupi",
    address: "Kinnimulki Main Road",
    capacity: 180,
    occupied: 180,
    status: "FULL",
    phone: "+91 820 252 3412",
  },
];

const seedRequests = [
  {
    id: "RR-2084",
    resource: "WATER",
    quantity: "2,000 L",
    location: "Mysuru District Relief Centre",
    status: "PENDING",
    requester: "Ananya Rao",
  },
  {
    id: "RR-2082",
    resource: "MEDICINE",
    quantity: "120 kits",
    location: "Sakleshpur field unit",
    status: "APPROVED",
    requester: "Rescue Team Alpha",
  },
  {
    id: "RR-2079",
    resource: "FOOD",
    quantity: "350 meals",
    location: "Jayanagar Community Hall",
    status: "DELIVERED",
    requester: "Kiran Shah",
  },
];

const navItems = [
  ["dashboard", "Overview"],
  ["incidents", "Incidents"],
  ["shelters", "Shelters"],
  ["requests", "Resource requests"],
  ["report", "Report incident"],
  ["admin", "Admin console"],
];

function App() {
  const [route, setRoute] = useState(
    window.location.hash.slice(1) || "dashboard",
  );
  const [user, setUser] = useState({
    name: "Aarav Mehta",
    role: "ADMIN",
    initials: "AM",
  });
  const [toast, setToast] = useState("");

  useEffect(() => {
    const onHash = () => setRoute(window.location.hash.slice(1) || "dashboard");
    window.addEventListener("hashchange", onHash);
    return () => window.removeEventListener("hashchange", onHash);
  }, []);

  const navigate = (nextRoute) => {
    window.location.hash = nextRoute;
  };
  const notify = (message) => {
    setToast(message);
    window.setTimeout(() => setToast(""), 3200);
  };

  if (route === "login" || route === "register")
    return (
      <AuthPage
        mode={route}
        onAuth={(nextUser) => {
          setUser(nextUser);
          navigate("dashboard");
        }}
      />
    );

  return (
    <div className="app-shell">
      <Sidebar route={route} navigate={navigate} user={user} />
      <main className="main-content">
        <Topbar route={route} user={user} navigate={navigate} />
        <div className="page-wrap">
          {route === "dashboard" && <Dashboard navigate={navigate} />}
          {route === "incidents" && (
            <Incidents navigate={navigate} notify={notify} />
          )}
          {route === "shelters" && <Shelters notify={notify} />}
          {route === "requests" && <Requests notify={notify} />}
          {route === "report" && (
            <ReportIncident notify={notify} navigate={navigate} />
          )}
          {route === "admin" && <Admin notify={notify} />}
          {![...navItems.map(([key]) => key), "login", "register"].includes(
            route,
          ) && <Dashboard navigate={navigate} />}
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

function Sidebar({ route, navigate, user }) {
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
        {navItems.map(([key, label], index) => (
          <button
            className={route === key ? "nav-item active" : "nav-item"}
            key={key}
            onClick={() => navigate(key)}
          >
            <span className="nav-icon">
              {["⌂", "◈", "⌖", "▤", "!", "⚙"][index]}
            </span>
            {label}
            {key === "incidents" && <em>4</em>}
          </button>
        ))}
      </nav>
      <div className="sidebar-footer">
        <div className="status-line">
          <span className="pulse" /> All systems operational
        </div>
        <div className="mini-user">
          <div className="avatar">{user.initials}</div>
          <div>
            <strong>{user.name}</strong>
            <span>{user.role.toLowerCase().replace("_", " ")}</span>
          </div>
          <span className="dots">•••</span>
        </div>
      </div>
    </aside>
  );
}

function Topbar({ route, user, navigate }) {
  const titles = {
    dashboard: [
      "Good morning, Aarav",
      "Here is what needs your attention today.",
    ],
    incidents: [
      "Incident centre",
      "Monitor, verify, and coordinate active reports.",
    ],
    shelters: [
      "Emergency shelters",
      "Live capacity and availability across the network.",
    ],
    requests: [
      "Resource requests",
      "Track the flow of relief from request to delivery.",
    ],
    report: [
      "Report an incident",
      "Share verified details so response teams can act quickly.",
    ],
    admin: [
      "Admin console",
      "Manage people, permissions, and platform operations.",
    ],
  };
  const [title, sub] = titles[route] || titles.dashboard;
  return (
    <header className="topbar">
      <div>
        <div className="eyebrow">THURSDAY, 03 SEPTEMBER 2026</div>
        <h1>{title}</h1>
        <p>{sub}</p>
      </div>
      <div className="top-actions">
        <button className="icon-button" aria-label="Notifications">
          ♢<i />
        </button>
        <button className="profile-button" onClick={() => navigate("admin")}>
          <div className="avatar">{user.initials}</div>
          <span>{user.name}</span>
          <b>⌄</b>
        </button>
      </div>
    </header>
  );
}

function Dashboard({ navigate }) {
  return (
    <>
      <section className="hero-banner">
        <div>
          <span className="tag orange">LIVE OPERATIONS</span>
          <h2>Clarity when every second counts.</h2>
          <p>
            One command view for incidents, shelters, and the people moving help
            forward.
          </p>
          <button className="primary-button" onClick={() => navigate("report")}>
            Report an incident <span>↗</span>
          </button>
        </div>
        <div className="hero-radar">
          <div className="radar-ring ring-one" />
          <div className="radar-ring ring-two" />
          <div className="radar-sweep" />
          <span className="radar-dot dot-one" />
          <span className="radar-dot dot-two" />
          <strong>08</strong>
          <small>
            ACTIVE
            <br />
            SIGNALS
          </small>
        </div>
      </section>
      <div className="stat-grid">
        <Stat
          label="Active incidents"
          value="08"
          trend="+2 today"
          accent="red"
        />
        <Stat
          label="People in shelters"
          value="978"
          trend="72% capacity"
          accent="yellow"
        />
        <Stat label="Open requests" value="24" trend="6 urgent" accent="blue" />
        <Stat
          label="Response teams"
          value="31"
          trend="All checked in"
          accent="green"
        />
      </div>
      <div className="dashboard-grid">
        <section className="panel incident-panel">
          <PanelHead
            title="Priority incidents"
            action="View all"
            onAction={() => navigate("incidents")}
          />
          <div className="incident-list">
            {seedIncidents.slice(0, 3).map((incident) => (
              <IncidentRow key={incident.id} incident={incident} />
            ))}
          </div>
        </section>
        <section className="panel">
          <PanelHead
            title="Shelter capacity"
            action="Explore shelters"
            onAction={() => navigate("shelters")}
          />
          <div className="capacity-summary">
            <div className="capacity-number">
              72<span>%</span>
            </div>
            <div>
              <strong>Network occupancy</strong>
              <p>978 of 1,350 beds in use</p>
            </div>
          </div>
          <div className="progress large">
            <span style={{ width: "72%" }} />
          </div>
          {seedShelters.slice(0, 2).map((shelter) => (
            <div className="capacity-row" key={shelter.name}>
              <span>{shelter.name}</span>
              <strong>
                {Math.round((shelter.occupied / shelter.capacity) * 100)}%
              </strong>
            </div>
          ))}
        </section>
      </div>
      <section className="panel activity-panel">
        <PanelHead
          title="Response activity"
          action="Open requests"
          onAction={() => navigate("requests")}
        />
        <div className="activity-list">
          {seedRequests.map((request, i) => (
            <div className="activity-row" key={request.id}>
              <div className={`activity-icon activity-${i}`}>
                {i === 0 ? "↓" : i === 1 ? "✓" : "↗"}
              </div>
              <div>
                <strong>
                  {request.resource} request <span>{request.id}</span>
                </strong>
                <p>
                  {request.location} · {request.quantity}
                </p>
              </div>
              <Status value={request.status} />
            </div>
          ))}
        </div>
      </section>
    </>
  );
}

function PanelHead({ title, action, onAction }) {
  return (
    <div className="panel-head">
      <h3>{title}</h3>
      <button className="text-button" onClick={onAction}>
        {action} <span>→</span>
      </button>
    </div>
  );
}
function Stat({ label, value, trend, accent }) {
  return (
    <div className="stat-card">
      <div className={`stat-accent ${accent}`} />
      <span>{label}</span>
      <strong>{value}</strong>
      <small className={accent === "red" ? "negative" : ""}>{trend}</small>
    </div>
  );
}
function IncidentRow({ incident, compact = false }) {
  return (
    <div className="incident-row">
      <div className={`severity-marker ${incident.severity.toLowerCase()}`} />
      <div className="incident-main">
        <div>
          <strong>{incident.title}</strong>
          <span className="location">⌖ {incident.location}</span>
        </div>
        {!compact && <p>{incident.description}</p>}
        <small>Updated {incident.updated}</small>
      </div>
      <Status value={incident.severity} />
    </div>
  );
}
function Status({ value }) {
  return (
    <span className={`status status-${value.toLowerCase()}`}>
      {value.replace("_", " ")}
    </span>
  );
}

function Incidents({ navigate, notify }) {
  const [query, setQuery] = useState("");
  const [filter, setFilter] = useState("ALL");
  const visible = seedIncidents.filter(
    (item) =>
      (filter === "ALL" || item.severity === filter) &&
      `${item.title} ${item.location}`
        .toLowerCase()
        .includes(query.toLowerCase()),
  );
  return (
    <>
      <div className="toolbar">
        <div className="search">
          <span>⌕</span>
          <input
            placeholder="Search by location or incident"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>
        <select value={filter} onChange={(e) => setFilter(e.target.value)}>
          <option value="ALL">All severity</option>
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
          <PanelHead
            title={`${visible.length} incidents in view`}
            action="Sort: newest"
          />
          <div className="incident-list full-list">
            {visible.map((incident) => (
              <button
                className="incident-button"
                key={incident.id}
                onClick={() => notify(`Incident #${incident.id} selected`)}
              >
                <IncidentRow incident={incident} />
              </button>
            ))}
          </div>
        </section>
        <aside className="panel map-panel">
          <div className="map-placeholder">
            <div className="map-grid" />
            <span className="map-label label-a">
              MYSURU <i />
            </span>
            <span className="map-label label-b">
              SAKLESHPUR <i />
            </span>
            <span className="map-label label-c">
              BENGALURU <i />
            </span>
            <div className="map-caption">
              <strong>Regional view</strong>
              <span>8 active signals</span>
            </div>
          </div>
        </aside>
      </div>
    </>
  );
}

function Shelters({ notify }) {
  const [city, setCity] = useState("");
  const visible = seedShelters.filter((s) =>
    `${s.name} ${s.city}`.toLowerCase().includes(city.toLowerCase()),
  );
  return (
    <>
      <div className="section-intro">
        <div>
          <span className="tag green">NETWORK STATUS · LIVE</span>
          <h2>Find a safe place to stay.</h2>
        </div>
        <div className="search">
          <span>⌕</span>
          <input
            placeholder="Search city or shelter"
            value={city}
            onChange={(e) => setCity(e.target.value)}
          />
        </div>
      </div>
      <div className="shelter-grid">
        {visible.map((shelter) => (
          <article className="shelter-card" key={shelter.name}>
            <div className="shelter-top">
              <span className="shelter-icon">⌂</span>
              <Status value={shelter.status} />
            </div>
            <h3>{shelter.name}</h3>
            <p className="muted">
              ⌖ {shelter.address}, {shelter.city}
            </p>
            <div className="beds">
              <div>
                <strong>{shelter.capacity - shelter.occupied}</strong>
                <span>beds available</span>
              </div>
              <div className="bed-total">
                {shelter.occupied} / {shelter.capacity}
              </div>
            </div>
            <div className="progress">
              <span
                style={{
                  width: `${(shelter.occupied / shelter.capacity) * 100}%`,
                }}
              />
            </div>
            <div className="shelter-footer">
              <span>☎ {shelter.phone}</span>
              <button
                className="text-button"
                onClick={() => notify(`Directions to ${shelter.name}`)}
              >
                Get directions →
              </button>
            </div>
          </article>
        ))}
      </div>
    </>
  );
}

function Requests({ notify }) {
  const [status, setStatus] = useState("ALL");
  const [showForm, setShowForm] = useState(false);
  const visible = seedRequests.filter(
    (item) => status === "ALL" || item.status === status,
  );
  return (
    <>
      {showForm ? (
        <RequestForm
          onCancel={() => setShowForm(false)}
          onSubmit={() => {
            setShowForm(false);
            notify("Resource request submitted");
          }}
        />
      ) : (
        <>
          <div className="toolbar">
            <div className="filter-tabs">
              {["ALL", "PENDING", "APPROVED", "DELIVERED"].map((item) => (
                <button
                  className={status === item ? "selected" : ""}
                  key={item}
                  onClick={() => setStatus(item)}
                >
                  {item === "ALL"
                    ? "All requests"
                    : item[0] + item.slice(1).toLowerCase()}
                </button>
              ))}
            </div>
            <button
              className="primary-button"
              onClick={() => setShowForm(true)}
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
                  <th>Destination</th>
                  <th>Requester</th>
                  <th>Status</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {visible.map((request) => (
                  <tr key={request.id}>
                    <td>
                      <strong>{request.id}</strong>
                    </td>
                    <td>
                      <span className="resource-label">{request.resource}</span>
                      <small>{request.quantity}</small>
                    </td>
                    <td>{request.location}</td>
                    <td>{request.requester}</td>
                    <td>
                      <Status value={request.status} />
                    </td>
                    <td>
                      <button
                        className="more-button"
                        onClick={() => notify(`Opened ${request.id}`)}
                      >
                        •••
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
function RequestForm({ onCancel, onSubmit }) {
  return (
    <section className="form-card">
      <div className="form-title">
        <span className="tag blue">RELIEF LOGISTICS</span>
        <h2>New resource request</h2>
        <p>Tell response teams what is needed and where it should go.</p>
      </div>
      <div className="form-grid">
        <label>
          Resource type
          <select>
            <option>WATER</option>
            <option>FOOD</option>
            <option>MEDICINE</option>
            <option>CLOTHES</option>
          </select>
        </label>
        <label>
          Quantity
          <input placeholder="e.g. 500 litres" />
        </label>
        <label className="wide">
          Destination
          <input placeholder="Shelter, field unit, or address" />
        </label>
        <label className="wide">
          Additional context
          <textarea placeholder="Add delivery notes or urgency details" />
        </label>
      </div>
      <div className="form-actions">
        <button className="secondary-button" onClick={onCancel}>
          Cancel
        </button>
        <button className="primary-button" onClick={onSubmit}>
          Submit request →
        </button>
      </div>
    </section>
  );
}

function ReportIncident({ notify, navigate }) {
  return (
    <section className="form-card report-card">
      <div className="report-side">
        <span className="tag orange">FIELD REPORT</span>
        <h2>Help us see what is happening.</h2>
        <p>
          Reports are reviewed by the operations centre before they are shared
          with response teams.
        </p>
        <div className="report-note">
          <strong>What makes a useful report?</strong>
          <span>
            Location, type of incident, severity, and what people need right
            now.
          </span>
        </div>
      </div>
      <div className="report-form">
        <label>
          Incident title
          <input placeholder="What is happening?" />
        </label>
        <div className="form-grid two">
          <label>
            Incident type
            <select>
              <option>Flood</option>
              <option>Fire</option>
              <option>Landslide</option>
              <option>Storm</option>
              <option>Other</option>
            </select>
          </label>
          <label>
            Severity
            <select>
              <option>MEDIUM</option>
              <option>LOW</option>
              <option>HIGH</option>
              <option>CRITICAL</option>
            </select>
          </label>
        </div>
        <label>
          Location
          <input placeholder="City, neighborhood, or landmark" />
        </label>
        <label>
          What do response teams need to know?
          <textarea
            rows="5"
            placeholder="Describe the situation, people affected, and immediate needs"
          />
        </label>
        <div className="form-actions">
          <button
            className="secondary-button"
            onClick={() => navigate("dashboard")}
          >
            Cancel
          </button>
          <button
            className="primary-button"
            onClick={() => notify("Incident report submitted for review")}
          >
            Submit report →
          </button>
        </div>
      </div>
    </section>
  );
}

function Admin({ notify }) {
  return (
    <>
      <div className="admin-banner">
        <div>
          <span className="tag red">ADMIN ONLY</span>
          <h2>People power the response.</h2>
          <p>Keep roles, access, and operational readiness in order.</p>
        </div>
        <button
          className="secondary-button"
          onClick={() => notify("Invite flow opened")}
        >
          + Invite user
        </button>
      </div>
      <div className="stat-grid admin-stats">
        <Stat
          label="Registered users"
          value="284"
          trend="+12 this month"
          accent="blue"
        />
        <Stat
          label="Volunteers"
          value="196"
          trend="69% of network"
          accent="green"
        />
        <Stat
          label="Rescue teams"
          value="31"
          trend="4 on standby"
          accent="yellow"
        />
        <Stat
          label="Pending reviews"
          value="07"
          trend="Needs attention"
          accent="red"
        />
      </div>
      <section className="panel table-panel">
        <PanelHead
          title="Recent user activity"
          action="Export list"
          onAction={() => notify("User list export prepared")}
        />
        <table>
          <thead>
            <tr>
              <th>User</th>
              <th>Role</th>
              <th>Location</th>
              <th>Last active</th>
              <th>Status</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {[
              ["Priya Nair", "VOLUNTEER", "Bengaluru", "2 min ago", "ACTIVE"],
              [
                "Rescue Team Alpha",
                "RESCUE_TEAM",
                "Mysuru",
                "8 min ago",
                "ACTIVE",
              ],
              ["Kiran Shah", "CITIZEN", "Udupi", "1 day ago", "ACTIVE"],
              [
                "Meera Joshi",
                "VOLUNTEER",
                "Mangaluru",
                "3 days ago",
                "PENDING",
              ],
            ].map((row) => (
              <tr key={row[0]}>
                <td>
                  <div className="user-cell">
                    <div className="avatar small">
                      {row[0]
                        .split(" ")
                        .map((part) => part[0])
                        .join("")}
                    </div>
                    <strong>{row[0]}</strong>
                  </div>
                </td>
                <td>
                  <span className="role-label">{row[1].replace("_", " ")}</span>
                </td>
                <td>{row[2]}</td>
                <td>{row[3]}</td>
                <td>
                  <Status value={row[4]} />
                </td>
                <td>
                  <button
                    className="more-button"
                    onClick={() => notify(`Managed ${row[0]}`)}
                  >
                    •••
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </>
  );
}

function AuthPage({ mode, onAuth }) {
  const register = mode === "register";
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
          “Preparedness turns uncertainty into options.”
        </div>
      </div>
      <div className="auth-panel">
        <button
          className="back-link"
          onClick={() => {
            window.location.hash = "dashboard";
          }}
        >
          ← Back to overview
        </button>
        <div className="auth-form">
          <span className="tag blue">HAVEN PLATFORM</span>
          <h2>{register ? "Create your account" : "Welcome back"}</h2>
          <p>
            {register
              ? "Join the response network in your community."
              : "Sign in to your operations workspace."}
          </p>
          {register && (
            <label>
              Full name
              <input placeholder="Your name" />
            </label>
          )}
          <label>
            Email address
            <input type="email" placeholder="you@example.com" />
          </label>
          <label>
            Password
            <input type="password" placeholder="••••••••" />
          </label>
          {register && (
            <label>
              Join as
              <select>
                <option>CITIZEN</option>
                <option>VOLUNTEER</option>
                <option>RESCUE TEAM</option>
              </select>
            </label>
          )}
          <button
            className="primary-button full"
            onClick={() =>
              onAuth({
                name: register ? "New member" : "Aarav Mehta",
                role: register ? "CITIZEN" : "ADMIN",
                initials: register ? "NM" : "AM",
              })
            }
          >
            {register ? "Create account" : "Sign in"} <span>→</span>
          </button>
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
