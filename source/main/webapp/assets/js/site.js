(function () {
  "use strict";

  // Mobile menu toggle
  var toggle = document.querySelector(".nav-toggle");
  var nav = document.getElementById("site-nav");
  if (toggle && nav) {
    toggle.addEventListener("click", function () {
      var open = nav.classList.toggle("open");
      toggle.setAttribute("aria-expanded", String(open));
    });
  }

  // Button ripple (DHTML effect)
  document.addEventListener("click", function (e) {
    var btn = e.target.closest(".btn");
    if (!btn) return;
    var r = btn.getBoundingClientRect();
    var size = Math.max(r.width, r.height);
    var dot = document.createElement("span");
    dot.className = "ripple";
    dot.style.width = dot.style.height = size + "px";
    dot.style.left = (e.clientX - r.left - size / 2) + "px";
    dot.style.top = (e.clientY - r.top - size / 2) + "px";
    btn.appendChild(dot);
    setTimeout(function () { dot.remove(); }, 600);
  });

  // Scroll reveal
  var items = document.querySelectorAll(".reveal");
  if ("IntersectionObserver" in window) {
    var io = new IntersectionObserver(function (entries) {
      entries.forEach(function (en) {
        if (en.isIntersecting) { en.target.classList.add("visible"); io.unobserve(en.target); }
      });
    });
    items.forEach(function (el) { io.observe(el); });
  } else {
    items.forEach(function (el) { el.classList.add("visible"); });
  }

  // Social links from JSON (textContent only, no HTML injection)
  var list = document.getElementById("social-links");
  if (list && list.dataset.src) {
    fetch(list.dataset.src).then(function (r) { return r.json(); }).then(function (data) {
      data.links.forEach(function (l) {
        if (!/^https:\/\//.test(l.url)) return;
        var li = document.createElement("li");
        var a = document.createElement("a");
        a.href = l.url; a.rel = "noopener noreferrer"; a.textContent = l.name;
        li.appendChild(a); list.appendChild(li);
      });
    }).catch(function () {});
  }
})();
