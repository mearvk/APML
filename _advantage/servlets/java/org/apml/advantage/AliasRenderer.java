package org.apml.advantage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Renders the site as static HTML, one page per major subject (the six technologies), plus a redirect
 * page at each assumed legacy Wix slug. Output goes to the directory given as the first argument,
 * normally _advantage/assumed-aliases. Run from the repository root:
 *
 *   javac -d /tmp/aa _advantage/servlets/java/org/apml/advantage/{Technology,Term,Catalog,AliasRenderer}.java
 *   java -cp /tmp/aa org.apml.advantage.AliasRenderer _advantage/assumed-aliases
 *
 * The pages link to css/site.css and js/site.js in the output directory, which are hand-kept and not generated.
 */
public final class AliasRenderer {
    private AliasRenderer() {}

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("usage: AliasRenderer <output-dir>");
            System.exit(2);
        }
        Path out = Path.of(args[0]);
        Catalog c = new Catalog();
        List<Technology> ts = c.getTechnologies();

        write(out.resolve("index.html"), page("Major subjects", "", indexBody(c), ts, null));
        for (Technology t : ts) {
            write(out.resolve(t.getSlug()).resolve("index.html"),
                  page(t.getName(), "../", subjectBody(c, t), ts, t.getSlug()));
            String legacy = c.legacySlug(t.getSlug());
            write(out.resolve(legacy).resolve("index.html"), redirect(t, legacy));
        }
        System.out.println("rendered " + ts.size() + " subjects and " + ts.size() + " alias pages into " + out);
    }

    private static String indexBody(Catalog c) {
        StringBuilder b = new StringBuilder();
        b.append("  <h1>Major subjects</h1>\n");
        b.append("  <p class=\"lead\">The six technologies, one page each, in reading order. The last hands back to the first.</p>\n");
        b.append("  <ul class=\"cards\">\n");
        for (Technology t : c.getTechnologies()) {
            b.append("    <li class=\"card\"><span class=\"num\">Strand ").append(t.getOrder()).append("</span>\n");
            b.append("      <h3><a href=\"").append(t.getSlug()).append("/\">").append(esc(t.getName())).append("</a></h3>\n");
            b.append("      <p>").append(esc(t.getRole())).append("</p>\n");
            b.append("      <p class=\"alias\">Assumed old address: <code>").append(esc(c.legacySlug(t.getSlug()))).append("</code></p></li>\n");
        }
        b.append("  </ul>\n");
        b.append("  <p class=\"note\">The old addresses are assumptions from analysis/APML-ANALYSIS.md section 5a: they hold only if the owner's page numbering follows the order of the original URL list. Each old address redirects to its subject page.</p>\n");
        return b.toString();
    }

    private static String subjectBody(Catalog c, Technology t) {
        List<Technology> all = c.getTechnologies();
        Technology prev = all.get((t.getOrder() + all.size() - 2) % all.size());
        Technology next = c.find(t.getHandsOffTo());
        Technology pair = c.find(t.getPairedWith());
        StringBuilder b = new StringBuilder();
        b.append("  <article>\n");
        b.append("    <p class=\"num\">Strand ").append(t.getOrder()).append(" of ").append(all.size()).append("</p>\n");
        b.append("    <h1>").append(esc(t.getName())).append("</h1>\n");
        b.append("    <p class=\"lead\">").append(esc(t.getRole())).append("</p>\n");
        b.append("    <h2>Question</h2>\n    <p>").append(esc(t.getQuestion())).append("</p>\n");
        b.append("    <h2>Principle</h2>\n    <p class=\"emphasis\">").append(esc(t.getPrinciple())).append("</p>\n");
        b.append("    <h2>What the owner says it does</h2>\n").append(list(t.getOwnerClaims()));
        b.append("    <h2>Still open</h2>\n").append(list(t.getOpenItems()));
        if (t.getSlug().equals("sprung")) {
            b.append("    <h2>Sprung terms</h2>\n");
            b.append("    <p>The owner's own terms, recorded as described. No formula has been invented for any of them.</p>\n");
            b.append("    <table>\n      <thead><tr><th scope=\"col\">Term</th><th scope=\"col\">Owner description</th><th scope=\"col\">Status</th></tr></thead>\n      <tbody>\n");
            for (Term m : c.getTerms()) {
                b.append("        <tr><th scope=\"row\">").append(esc(m.getName())).append("</th><td>")
                 .append(esc(m.getOwnerDescription() == null ? "Not yet written out." : m.getOwnerDescription()))
                 .append("</td><td>").append(esc(m.getStatus())).append("</td></tr>\n");
            }
            b.append("      </tbody>\n    </table>\n");
        }
        b.append("    <h2>Where it sits</h2>\n    <ul>\n");
        b.append("      <li>Receives from: <a href=\"../").append(prev.getSlug()).append("/\">").append(esc(prev.getName())).append("</a></li>\n");
        b.append("      <li>Hands off to: <a href=\"../").append(next.getSlug()).append("/\">").append(esc(next.getName())).append("</a></li>\n");
        b.append("      <li>Faces, in the middle fold: <a href=\"../").append(pair.getSlug()).append("/\">").append(esc(pair.getName())).append("</a></li>\n");
        b.append("      <li>Assumed old address: <code>").append(esc(c.legacySlug(t.getSlug()))).append("</code></li>\n    </ul>\n");
        b.append("  </article>\n");
        b.append("  <div class=\"actions\">\n");
        b.append("    <a class=\"btn btn-ghost\" href=\"../\">All subjects</a>\n");
        b.append("    <a class=\"btn btn-primary\" href=\"../../api/v1/technologies/").append(t.getSlug()).append("\">This subject as JSON</a>\n");
        b.append("  </div>\n");
        return b.toString();
    }

    private static String list(List<String> items) {
        StringBuilder b = new StringBuilder("    <ul>\n");
        for (String s : items) b.append("      <li>").append(esc(s)).append("</li>\n");
        return b.append("    </ul>\n").toString();
    }

    private static String page(String title, String root, String body, List<Technology> ts, String active) {
        StringBuilder b = new StringBuilder();
        b.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n  <meta charset=\"UTF-8\">\n");
        b.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
        b.append("  <title>").append(esc(title)).append(" | APML Advantage</title>\n");
        b.append("  <link rel=\"stylesheet\" href=\"").append(root).append("css/site.css\">\n");
        b.append("  <script defer src=\"").append(root).append("js/site.js\"></script>\n</head>\n<body>\n");
        b.append("<a class=\"skip-link\" href=\"#main\">Skip to content</a>\n<header class=\"site-header\">\n");
        b.append("  <a class=\"brand\" href=\"").append(root.isEmpty() ? "./" : root).append("\">APML Advantage</a>\n");
        b.append("  <button type=\"button\" class=\"btn nav-toggle\" aria-expanded=\"false\" aria-controls=\"site-nav\">Menu</button>\n");
        b.append("  <nav id=\"site-nav\" class=\"site-nav\" aria-label=\"Major subjects\">\n");
        for (Technology t : ts) {
            b.append("    <a href=\"").append(root).append(t.getSlug()).append("/\"");
            if (t.getSlug().equals(active)) b.append(" aria-current=\"page\"");
            b.append(">").append(esc(t.getName())).append("</a>\n");
        }
        b.append("  </nav>\n</header>\n<main id=\"main\">\n").append(body).append("</main>\n");
        b.append("<footer class=\"site-footer\">\n  <p>APML: New Engaging Software for the Net Universe.</p>\n");
        b.append("  <p>Also: <a href=\"").append(root).append("../html/index.html\">HTML</a> | <a href=\"")
         .append(root).append("../jsp/index.jsp\">JSP</a> | <a href=\"").append(root).append("../console\">Servlet console</a></p>\n");
        b.append("</footer>\n</body>\n</html>\n");
        return b.toString();
    }

    private static String redirect(Technology t, String legacy) {
        String target = "../" + t.getSlug() + "/";
        return "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n  <meta charset=\"UTF-8\">\n"
            + "  <title>Moved to " + esc(t.getName()) + " | APML Advantage</title>\n"
            + "  <meta http-equiv=\"refresh\" content=\"0; url=" + target + "\">\n"
            + "  <link rel=\"canonical\" href=\"" + target + "\">\n"
            + "  <meta name=\"robots\" content=\"noindex\">\n"
            + "  <link rel=\"stylesheet\" href=\"../css/site.css\">\n</head>\n<body>\n<main>\n"
            + "  <h1>Moved</h1>\n  <p>The assumed old address <code>" + esc(legacy) + "</code> is now <a href=\""
            + target + "\">" + esc(t.getName()) + "</a>.</p>\n</main>\n</body>\n</html>\n";
    }

    private static void write(Path p, String s) throws IOException {
        Files.createDirectories(p.getParent());
        Files.writeString(p, s, StandardCharsets.UTF_8);
    }

    private static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
