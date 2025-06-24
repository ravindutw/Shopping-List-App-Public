const API_BASE = "/app-api/groceries";
let csrfToken = null;
let csrfHeaderName = "X-CSRF-TOKEN"; // default header name

// On load, fetch CSRF token and then start app
document.addEventListener("DOMContentLoaded", async function () {
    setDarkTheme();
    await fetchCsrfToken(); // Fetch and store CSRF token
    fetchGroceries();
    setInterval(fetchGroceries, 10000);

    document.getElementById("add-input").addEventListener("input", updateAddButtonState);
    updateAddButtonState();
});

function setDarkTheme() {
    document.documentElement.setAttribute("data-theme", "dark");
}

// Fetch CSRF token from server
async function fetchCsrfToken() {
    try {
        const response = await fetch('/csrf-token', {
            credentials: 'include'
        });
        const data = await response.json();
        csrfToken = data.token;
        csrfHeaderName = data.headerName;
    } catch (error) {
        console.error("Failed to fetch CSRF token", error);
    }
}

// Fetch and render groceries
function fetchGroceries() {
    fetch(API_BASE + "/fetch", { credentials: 'include' })
        .then(res => res.json())
        .then(data => {
            const groceries = data.map(item => ({
                ...item,
                checked: item.checked === true || item.checked === "true"
            }));
            renderGroceries(groceries);
        })
        .catch(() => {
            renderGroceries([]);
        });
}

function renderGroceries(groceries) {
    const main = document.getElementById("main-content");
    if (!groceries || groceries.length === 0) {
        main.innerHTML = `<div class="empty">No items yet. Add some groceries!</div>`;
        return;
    }
    main.innerHTML = `<ul class="grocery-list">
    ${groceries.map(item => `
      <li class="${item.checked ? "checked" : ""}">
        <label class="custom-radio-label">
          <input type="radio" class="custom-radio" ${item.checked ? "checked" : ""}
            onchange="toggleCheck('${item.id}', ${item.checked})">
          <span class="custom-radio-circle" aria-hidden="true"></span>
          <span>${escapeHtml(item.name)}</span>
        </label>
      </li>
    `).join("")}
  </ul>`;
}

// Escape HTML
function escapeHtml(text) {
    return text.replace(/[&<>"']/g, function (m) {
        return ({
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#39;'
        })[m];
    });
}

// Add grocery item
function addGroceryItem() {
    const input = document.getElementById("add-input");
    const btn = document.getElementById("add-btn");
    const val = input.value.trim();
    if (!val || !csrfToken) return;

    btn.disabled = true;
    input.disabled = true;

    fetch(`${API_BASE}/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [csrfHeaderName]: csrfToken
        },
        body: JSON.stringify({ name: val }),
        credentials: "include"
    })
        .then(() => {
            input.value = "";
            fetchGroceries();
        })
        .finally(() => {
            btn.disabled = false;
            input.disabled = false;
            updateAddButtonState();
        });
}

// Toggle check
function toggleCheck(id, checked) {
    if (!csrfToken) return;

    fetch(`${API_BASE}/toggle`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [csrfHeaderName]: csrfToken
        },
        body: JSON.stringify({ id }),
        credentials: "include"
    })
        .then(() => fetchGroceries());
}

// Enable/disable add button
function updateAddButtonState() {
    const input = document.getElementById("add-input");
    const btn = document.getElementById("add-btn");
    btn.disabled = !input.value.trim();
}

// Logout
function logout() {
    if (!csrfToken) return;

    fetch("/logout", {
        method: "POST",
        headers: {
            [csrfHeaderName]: csrfToken
        },
        credentials: "include"
    })
        .finally(() => {
            window.location.href = "/login";
        });
}
