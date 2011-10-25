package perseus.document;

import perseus.language.LanguageCode;
import perseus.util.*;

public class SearchLinksTokenFilter extends TokenFilter {

	private String textLanguage;

	public SearchLinksTokenFilter (String languageCode) {
		textLanguage = languageCode;
	}

	public void process(Token token) {
		if (token.getType() == Token.Type.WORD &&
				! token.getLanguageCode().equals(LanguageCode.ENGLISH)) {

			StringBuffer newToken = new StringBuffer();
			newToken.append("<a href=\"searchresults?target=")
			.append(token.getLanguageCode())
			.append("&q=")
			.append(token.getOriginalText())
			.append("\" ");
			if (token.getLanguageCode().equals(textLanguage)) {
				newToken.append("class=\"text\" ");
			}
			newToken.append(">")
			.append(token.getDisplayText())
			.append("</a>");

			token.setDisplayText(newToken.toString());
		}
	}
}
