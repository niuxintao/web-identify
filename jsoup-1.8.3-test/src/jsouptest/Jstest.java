package jsouptest;

import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class Jstest {
	public boolean mustCorrectlyMoveChildrenInsideOneParentElement(
			boolean onebyone) {

		Document doc = new Document("");
		Element body = doc.appendElement("body");
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

		String result = doc.toString().replaceAll("\\s+", "");
		System.out.println(result);
		if ("<body><div3></div3><div4></div4><div1></div1><div2></div2></body>"
				.equals(result))
			return true;
		else
			return false;
	}

	public boolean testHash(boolean addedChildOrNot) {
		Element root = new Element(Tag.valueOf("root"), "");

		HashSet<Element> set = new HashSet<>();
		// Add root node:
		set.add(root);

		if (addedChildOrNot)
			root.appendChild(new Element(Tag.valueOf("a"), ""));

		return set.contains(root);
	}

	public static void main(String[] args) {
		Jstest jt = new Jstest();

		System.out.println(jt
				.mustCorrectlyMoveChildrenInsideOneParentElement(true));

		System.out.println(jt.testHash(false));
	}
}
