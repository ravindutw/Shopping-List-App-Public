import React, { useEffect, useState } from "react";
import "./App.css";

// Types
type GroceryItem = {
  id: string;
  name: string;
  checked: boolean;
};

// API endpoints (you may want to change these as needed)
const API_BASE = "http://localhost:4000/api/groceries";

// Mock data to use when backend is not ready
const MOCK_GROCERIES: GroceryItem[] = [
  { id: "1", name: "Milk", checked: false },
  { id: "2", name: "Bread", checked: true },
  { id: "3", name: "Eggs", checked: false },
];

function App() {
  const [groceries, setGroceries] = useState<GroceryItem[]>([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const [adding, setAdding] = useState(false);
  //const [theme, setTheme] = useState<"light" | "dark">("light");

  // Feature flag: Use mock data if backend is not ready
  const USE_MOCK = true;

  // Theme switching effect
  useEffect(() => {
    document.documentElement.setAttribute("data-theme", "dark");
  }, []);

  // Fetch groceries list on load
  useEffect(() => {
    setLoading(true);
    if (USE_MOCK) {
      // Simulate async fetch
      setTimeout(() => {
        setGroceries(MOCK_GROCERIES);
        setLoading(false);
      }, 500);
    } else {
      fetch(API_BASE)
        .then((res) => res.json())
        .then((data) => {
          setGroceries(data);
          setLoading(false);
        });
    }
  }, []);

  // Add new grocery item
  const handleAdd = async () => {
    if (!input.trim()) return;
    setAdding(true);
    const currentInput = input;
    setInput(""); // clear input immediately
    if (USE_MOCK) {
      setTimeout(() => {
        const newItem: GroceryItem = {
          id: (Math.random() * 100000).toFixed(0),
          name: currentInput,
          checked: false,
        };
        setAdding(false);
      }, 300);
    } else {
      await fetch(API_BASE, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `name=${encodeURIComponent(currentInput)}`,
      });
      setAdding(false);
    }
  };

  // Toggle check state
  const handleCheck = async (item: GroceryItem) => {
    if (USE_MOCK) {
      setGroceries((prev) =>
        prev.map((g) =>
          g.id === item.id ? { ...g, checked: !g.checked } : g
        )
      );
    } else {
      await fetch(`${API_BASE}/toggle`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `id=${encodeURIComponent(item.id)}`,
    });
      setGroceries((prev) =>
        prev.map((g) =>
          g.id === item.id ? { ...g, checked: !g.checked } : g
        )
      );
    }
  };

  /*
  const handleThemeToggle = () => {
    setTheme((prev) => (prev === "light" ? "dark" : "light"));
  };
  */

  /*
  // Removed theme change button
<button
    className={`theme-switch${theme === "dark" ? " dark" : ""}`}
    onClick={handleThemeToggle}
    aria-label="Toggle light/dark theme"
  >
    <span className="theme-switch-slider"></span>
</button>
  */

  return (
    <div className="container">
      <header>
        <h1>Grocery List</h1>
        
      </header>
      <main>
        {loading ? (
          <div className="loading">Loading...</div>
        ) : groceries.length === 0 ? (
          <div className="empty">No items yet. Add some groceries!</div>
        ) : (
          <ul className="grocery-list">
            {groceries.map((item) => (
              <li key={item.id} className={item.checked ? "checked" : ""}>
                <label className="custom-radio-label">
                  <input
                    type="radio"
                    className="custom-radio"
                    checked={item.checked}
                    onChange={() => handleCheck(item)}
                  />
                  <span
                    className="custom-radio-circle"
                    aria-hidden="true"
                  ></span>
                  <span>{item.name}</span>
                </label>
              </li>
            ))}
          </ul>
        )}
      </main>
      <footer>
        <div className="add-bar">
          <input
            type="text"
            placeholder="Add grocery item..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") handleAdd();
            }}
            disabled={adding}
          />
          <button
            className="add-btn"
            onClick={handleAdd}
            disabled={adding || !input.trim()}
            aria-label="Add item"
          >
            +
          </button>
        </div>
      </footer>
    </div>
  );
}

export default App;