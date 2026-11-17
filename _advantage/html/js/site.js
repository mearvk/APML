(function () {
  "use strict";

  // Mobile menu
  var toggle = document.querySelector(".nav-toggle");
  var nav = document.getElementById("site-nav");
  if (toggle && nav) {
    toggle.addEventListener("click", function () {
      var open = nav.classList.toggle("open");
      toggle.setAttribute("aria-expanded", String(open));
    });
  }

  var base = document.body.getAttribute("data-api");
  if (!base) return;

  function get(path) {
    return fetch(base + path, { headers: { Accept: "application/json" } }).then(function (r) {
      if (!r.ok) throw new Error("HTTP " + r.status);
      return r.json();
    });
  }
  function el(tag, cls, text) {
    var e = document.createElement(tag);
    if (cls) e.className = cls;
    if (text !== undefined) e.textContent = text;
    return e;
  }
  function fail(target, what) {
    target.replaceChildren(el("p", "note", "Could not load " + what + " from the API. Is the server running?"));
  }

  // Technology cards
  var cards = document.getElementById("technology-cards");
  if (cards) {
    get("/technologies").then(function (d) {
      var items = d.technologies.map(function (t) {
        var li = el("li", "card");
        li.appendChild(el("span", "num", "Strand " + t.order));
        var h = el("h3");
        var a = el("a", null, t.name);
        a.href = "technology.html?slug=" + encodeURIComponent(t.slug);
        h.appendChild(a);
        li.appendChild(h);
        li.appendChild(el("p", null, t.role));
        return li;
      });
      cards.replaceChildren.apply(cards, items);
    }).catch(function () { fail(cards, "the technologies"); });
  }

  // One technology
  var detail = document.getElementById("technology-detail");
  if (detail) {
    var slug = new URLSearchParams(location.search).get("slug") || "";
    get("/technologies/" + encodeURIComponent(slug)).then(function (t) {
      document.title = t.name + " | APML Advantage";
      var nodes = [el("h1", null, t.name), el("p", "lead", t.role)];
      nodes.push(el("h2", null, "Question"), el("p", null, t.question));
      nodes.push(el("h2", null, "Principle"), el("p", "emphasis", t.principle));
      nodes.push(el("h2", null, "What the owner says it does"));
      var ul = el("ul");
      t.ownerClaims.forEach(function (c) { ul.appendChild(el("li", null, c)); });
      nodes.push(ul);
      nodes.push(el("h2", null, "Still open"));
      var ol = el("ul");
      t.openItems.forEach(function (c) { ol.appendChild(el("li", null, c)); });
      nodes.push(ol);
      nodes.push(el("p", null, "Hands off to: " + t.handsOffTo + ". Paired with: " + t.pairedWith + "."));
      detail.replaceChildren.apply(detail, nodes);
    }).catch(function () { fail(detail, "this technology"); });
  }

  // Terms table
  var terms = document.getElementById("terms-body");
  if (terms) {
    get("/terms").then(function (d) {
      var rows = d.terms.map(function (t) {
        var tr = el("tr");
        tr.appendChild(el("th", null, t.name));
        tr.appendChild(el("td", null, t.ownerDescription || "Not yet written out."));
        tr.appendChild(el("td", null, t.status));
        return tr;
      });
      terms.replaceChildren.apply(terms, rows);
    }).catch(function () { fail(terms.parentNode.parentNode, "the terms"); });
  }

  // Health badge
  var health = document.getElementById("health");
  if (health) {
    get("/health").then(function (d) { health.textContent = "API: " + d.status; })
      .catch(function () { health.textContent = "API: unreachable"; });
  }
})();
