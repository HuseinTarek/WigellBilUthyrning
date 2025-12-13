// This script now only handles showing an error message if the login fails.
// The login process itself is handled by the form submission to Spring Security.

document.addEventListener("DOMContentLoaded", () => {
    if (window.location.search.includes("error")) {
        document.getElementById("loginError").textContent =
            "Fel användarnamn eller lösenord.";
    }
});

