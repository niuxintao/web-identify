package jsouptest;

import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.select.Elements;

import output.OutputSet;

public class Jstest {

	final String[] charset = { "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE",
			"UTF-16LE", "UTF-16" };

	public void nonsenseParamters(boolean selectOrNot, boolean ParseOrNot,
			boolean checkEqual) {
		String demo = "<select id='list'><option value='0'>First value</option><option value='1'>Second value</option><option value='2'>Third value</option></select>";

		if (ParseOrNot) {
			Document document = Jsoup.parse(demo);
			if (selectOrNot) {
				Elements options = document.select("select > option");

				for (Element element : options) {
					if (checkEqual) {
						if (element.text().equalsIgnoreCase("second value")) {
							return;
						}
					}
				}
			}
		}
	}

	public boolean test(int chars, boolean xmlOrHmtl) {
		Document document = Document.createShell("");
		if (xmlOrHmtl)
			document.outputSettings().syntax(Syntax.xml);
		else
			document.outputSettings().syntax(Syntax.html);
		document.charset(Charset.forName(charset[chars]));
		// System.out.println(document.html());

		String error = "<?";
		if (document.html().contains(error)) {
			if (!document.html().contains("?>"))
				return true; // hit the failure.
			else
				return false;
		} else
			return false;

	}

	public void parseSelectorException(boolean divOrTitle,
			boolean unbanlancedOrNot) {
		Document doc = new Document("");
		Element body = doc.appendElement("body");
		body.appendElement("div1");
		body.appendElement("div2");
		if (divOrTitle) {
			if (unbanlancedOrNot)
				doc.select("div.card-content2:has(a.subtitle[title= MySubTitle:)]");
			else
				doc.select("div.card-content2:has(a.subtitle[title= MySubTitle:])");
		} else {
			if (unbanlancedOrNot)
				doc.select("a.title[title=MyTitle :]]");
			else
				doc.select("a.title[title=MyTitle :]");
		}

	}

	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0] == 0 ? false : true, set[1] == 0 ? false
				: true, set[2] == 0 ? false : true);

		if (test(set[3], set[4] == 0 ? false : true)) {
			throw new Exception("Contains Invalid Declaration String!");
		}

		parseSelectorException(set[5] == 0 ? false : true, set[6] == 0 ? false
				: true);

		return OutputSet.PASS;
	}

	public static void main(String[] args) {
		Jstest jt = new Jstest();

		System.out.println(jt.test(1, true));

		jt.parseSelectorException(true, true);
	}
}
