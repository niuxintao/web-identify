package jsouptest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import output.OutputSet;

public class Jstest {

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

	public boolean mustCorrectlyMoveChildrenInsideOneParentElement(
			boolean reInsertOrNot, boolean onebyone) {

		Document doc = new Document("");
		Element body = doc.appendElement("body");

		if (reInsertOrNot) {
			body.appendElement("div1");
			body.appendElement("div2");
			Element div3 = body.appendElement("div3");
			Element div4 = body.appendElement("div4");

			if (!onebyone) {
				ArrayList<Element> toMove = new ArrayList<Element>() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					{
						add(div3);
						add(div4);
					}
				};

				body.insertChildren(0, toMove);
			} else {
				ArrayList<Element> toMove = new ArrayList<Element>() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					{
						add(div4);
					}
				};
				body.insertChildren(0, toMove);

				ArrayList<Element> toMove2 = new ArrayList<Element>() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					{
						add(div3);
					}
				};

				body.insertChildren(0, toMove2);
			}

		} else {
			body.appendElement("div3");
			body.appendElement("div4");
			body.appendElement("div1");
			body.appendElement("div2");
		}

		String result = doc.toString().replaceAll("\\s+", "");
//		System.out.println(result);
		if ("<body><div3></div3><div4></div4><div1></div1><div2></div2></body>"
				.equals(result))
			return true;
		else
			return false;
	}

	public boolean testHash(int setTypes, boolean addedChildOrNot,
			boolean appendStringOrNot, boolean appendElement) {
		Element root = new Element(Tag.valueOf("root"), "");

		Collection<Element> set = null;

		if (setTypes == 0)
			set = new HashSet<>();
		else if (setTypes == 1)
			set = new LinkedHashSet<>();
		else if (setTypes == 2)
			set = new ArrayList<>();
		// Add root node:
		set.add(root);

		if (addedChildOrNot)
			root.appendChild(new Element(Tag.valueOf("a"), ""));

		if (appendStringOrNot)
			root.appendText("a");

		if (appendElement)
			root.appendElement("a");

		return set.contains(root);
	}

	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0] == 0 ? false : true, set[1] == 0 ? false
				: true, set[2] == 0 ? false : true);

		if (!mustCorrectlyMoveChildrenInsideOneParentElement(
				set[3] == 0 ? false : true, set[4] == 0 ? false : true)) {
			throw new Exception("div sequences is not as expected !");
		}

		if (!testHash(set[5], set[6] == 0 ? false : true, set[7] == 0 ? false
				: true, set[8] == 0 ? false : true)) {
			throw new Exception("set cannot find the element as expected !");
		}

		return OutputSet.PASS;
	}

	public static void main(String[] args) {
		Jstest jt = new Jstest();

		jt.nonsenseParamters(true, true, true);
		System.out.println(jt.mustCorrectlyMoveChildrenInsideOneParentElement(
				false, true));

		System.out.println(jt.testHash(1, false, false, false));
	}
}
