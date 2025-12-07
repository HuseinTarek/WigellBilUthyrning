// This script now only handles showing an error message if the login fails.
// The login process itself is handled by the form submission to Spring Security.

document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
        const loginError = document.getElementById("loginError");
        if (loginError) {
            loginError.textContent = "Fel användarnamn eller lösenord.";
        }
    }
});
