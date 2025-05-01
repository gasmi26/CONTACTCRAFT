console.log("Script loaded on:", window.location.pathname);

// Get current theme from localStorage
let currentTheme = getTheme();

// Apply theme on page load
document.addEventListener("DOMContentLoaded", () => {
  applyTheme(currentTheme);

  const changeThemeButton = document.querySelector("#theme_change_button");
  if (changeThemeButton) {
    changeThemeButton.addEventListener("click", toggleTheme);
  }
});

// Toggle theme on button click
function toggleTheme() {
  let oldTheme = currentTheme;
  currentTheme = currentTheme === "dark" ? "light" : "dark";
  console.log("Theme changed to:", currentTheme);
  applyTheme(currentTheme, oldTheme);
}

// Apply theme changes
function applyTheme(theme, oldTheme = "") {
  setTheme(theme);

  if (oldTheme) {
    document.documentElement.classList.remove(oldTheme);
  }
  document.documentElement.classList.add(theme);

  // Update button text
  const changeThemeButton = document.querySelector("#theme_change_button");
  if (changeThemeButton) {
    changeThemeButton.querySelector("span").textContent =
      theme === "light" ? "Dark" : "Light";
  }
}

// Store theme in localStorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// Retrieve theme from localStorage (default: light)
function getTheme() {
  return localStorage.getItem("theme") || "light";
}



