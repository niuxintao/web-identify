package jsouptest;

import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document.OutputSettings.Syntax;

public class Jstest {

	public boolean test(boolean xmlOrHmtl) {
		Document document = Document.createShell("");
		if (xmlOrHmtl)
			document.outputSettings().syntax(Syntax.xml);
		else
			document.outputSettings().syntax(Syntax.html);
		document.charset(Charset.forName("utf-8"));
		System.out.println(document.html());

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
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Jstest jt = new Jstest();

		System.out.println(jt.test(true));

		jt.parseSelectorException(true, true);
	}
}
