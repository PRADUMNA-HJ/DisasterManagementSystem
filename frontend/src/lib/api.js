const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/v1";

async function request(path, options = {}) {
  const token = localStorage.getItem("haven_token");
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      ...(options.body ? { "Content-Type": "application/json" } : {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  });
  const body = await response.json().catch(() => ({}));
  if (!response.ok || body.success === false) {
    throw new Error(body.message || `Request failed (${response.status})`);
  }
  return body.data;
}

const json = (method, payload) => ({ method, body: JSON.stringify(payload) });

export const api = {
  info: () => request("/info"),
  login: (payload) => request("/auth/login", json("POST", payload)),
  register: (payload) => request("/auth/register", json("POST", payload)),
  me: () => request("/auth/me"),

  disasters: () => request("/disasters/all"),
  disaster: (id) => request(`/disasters/${id}`),
  createDisaster: (payload) => request("/disasters", json("POST", payload)),
  updateDisaster: (id, payload) =>
    request(`/disasters/${id}`, json("PUT", payload)),
  deleteDisaster: (id) => request(`/disasters/${id}`, { method: "DELETE" }),
  searchDisasters: (location) =>
    request(`/disasters/search?location=${encodeURIComponent(location)}`),
  filterDisasters: (severity) =>
    request(`/disasters/filter?severity=${encodeURIComponent(severity)}`),

  shelters: () => request("/shelters/all"),
  createShelter: (payload) => request("/shelters", json("POST", payload)),
  updateShelter: (id, payload) =>
    request(`/shelters/${id}`, json("PUT", payload)),
  deleteShelter: (id) => request(`/shelters/${id}`, { method: "DELETE" }),
  searchShelters: (city) =>
    request(`/shelters/search?city=${encodeURIComponent(city)}`),

  requests: () => request("/resource-requests/all"),
  createRequest: (payload) =>
    request("/resource-requests", json("POST", payload)),
  updateRequestStatus: (id, status) =>
    request(`/resource-requests/${id}/status`, json("PUT", { status })),
  deleteRequest: (id) =>
    request(`/resource-requests/${id}`, { method: "DELETE" }),

  logs: (disasterId) => request(`/incident-logs/disaster/${disasterId}/all`),
  createLog: (payload) => request("/incident-logs", json("POST", payload)),
  deleteLog: (id) => request(`/incident-logs/${id}`, { method: "DELETE" }),

  users: () => request("/users/all"),
  createUser: (payload) => request("/users", json("POST", payload)),
  updateUser: (id, payload) => request(`/users/${id}`, json("PUT", payload)),
  deleteUser: (id) => request(`/users/${id}`, { method: "DELETE" }),
};

export function isAuthenticated() {
  return Boolean(localStorage.getItem("haven_token"));
}

export function saveSession(auth) {
  localStorage.setItem("haven_token", auth.token);
  localStorage.setItem("haven_user", JSON.stringify(auth));
}

export function clearSession() {
  localStorage.removeItem("haven_token");
  localStorage.removeItem("haven_user");
}

export function storedUser() {
  try {
    return JSON.parse(localStorage.getItem("haven_user")) || null;
  } catch {
    return null;
  }
}
