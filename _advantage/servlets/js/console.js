(function () {
  "use strict";
  var form = document.getElementById("call");
  var sel = document.getElementById("resource");
  var out = document.getElementById("out");
  var status = document.getElementById("status");
  if (!form) return;

  form.addEventListener("submit", function (e) {
    e.preventDefault();
    var path = sel.value;
    status.className = "status";
    status.textContent = "Sending...";
    out.textContent = "";
    fetch(form.getAttribute("data-base") + path, { headers: { Accept: "application/json" } })
      .then(function (r) {
        status.textContent = "HTTP " + r.status + " " + (r.headers.get("Content-Type") || "");
        if (!r.ok) status.className = "status error";
        return r.json();
      })
      .then(function (j) { out.textContent = JSON.stringify(j, null, 2); })
      .catch(function (err) {
        status.className = "status error";
        status.textContent = "Request failed: " + err.message;
      });
  });
})();
