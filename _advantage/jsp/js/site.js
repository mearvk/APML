(function () {
  "use strict";

  var toggle = document.querySelector(".nav-toggle");
  var nav = document.getElementById("site-nav");
  if (toggle && nav) {
    toggle.addEventListener("click", function () {
      var open = nav.classList.toggle("open");
      toggle.setAttribute("aria-expanded", String(open));
    });
  }

  // The JSP pages are rendered on the server. This script only adds the live API status.
  var base = document.body.getAttribute("data-api");
  var health = document.getElementById("health");
  if (base && health) {
    fetch(base + "/health", { headers: { Accept: "application/json" } })
      .then(function (r) { if (!r.ok) throw new Error("HTTP " + r.status); return r.json(); })
      .then(function (d) { health.textContent = "API: " + d.status; })
      .catch(function () { health.textContent = "API: unreachable"; });
  }
})();
