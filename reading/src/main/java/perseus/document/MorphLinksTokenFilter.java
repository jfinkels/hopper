package perseus.document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import perseus.language.LanguageCode;
import perseus.util.Stoplist;
import perseus.util.StringUtil;
import edu.unc.epidoc.transcoder.TransCoder;

/**
 * This class converts tokens into HTML links to the morph.jsp page
 */
public class MorphLinksTokenFilter extends TokenFilter {

	private static Logger logger = Logger.getLogger(MorphLinksTokenFilter.class);
	private static final Pattern UNICODE_PATTERN =
		Pattern.compile("\\p{InGreek}|\\p{InGreekExtended}");

	private static Pattern filterPattern;
	static {
		Set<String> morphLanguages = new HashSet<String>();
		for (String language : LanguageCode.getKnownLanguages()) {
			if (LanguageCode.hasMorphData(language)) {
				morphLanguages.add(language);
			}
		}

		String patternString = "class=\"[^\"]*\\b(?:"
			+ StringUtil.join(morphLanguages, "|")
			+ ")\\b[^\"]*\"";
		filterPattern = Pattern.compile(patternString);
	}

	private String serviceURL = "morph";
	private String textLanguage;

	private String documentID = null;

	private String previousWord = null;
	private Stoplist previousWordStoplist = null;

	private boolean opensInNewWindow = false;

	private int documentsProcessed = 0;

	public MorphLinksTokenFilter () {
	}

	public MorphLinksTokenFilter (Metadata metadata) {
		textLanguage = metadata.get(Metadata.LANGUAGE_KEY);
		documentID = metadata.getDocumentID();
		opensInNewWindow = true;
	}

	public MorphLinksTokenFilter(Chunk chunk) {
		textLanguage = chunk.getEffectiveLanguage();
		documentID = chunk.getDocumentID();
		opensInNewWindow = true;
	}

	public MorphLinksTokenFilter (String languageCode) {
		textLanguage = languageCode;
	}

	public MorphLinksTokenFilter (Metadata metadata, String query) {
		this(metadata);

		//this.query = query;
	}

	public void setServiceURL(String s) {
		serviceURL = s;
	}

	public void setLanguage(String s) {
		textLanguage = s;
	}

	public void setDocumentID(String s) {
		documentID = s;
	}

	public void setPreviousWordStoplist(Stoplist stoplist) {
		previousWordStoplist = stoplist;
	}

	public void process(Token token) {
		if (token.getType() == Token.Type.WORD &&
				! token.getLanguageCode().equals(LanguageCode.ENGLISH)) {
			StringBuffer newToken = new StringBuffer();
			String encodedToken = token.getOriginalText();
			try {
				if (UNICODE_PATTERN.matcher(encodedToken).find()) {
					encodedToken = new TransCoder("Unicode", "BetaCode")
					.getString(encodedToken).toLowerCase();
				}
				encodedToken = URLEncoder.encode(encodedToken, "utf-8");
			} catch (UnsupportedEncodingException uee) {
				logger.warn("Problem encoding token: " + token.getOriginalText());
				encodedToken = token.getOriginalText();
			} catch (Exception e) {
				logger.warn("Problem transcoding: " + token.getOriginalText() + ": " + e);
				encodedToken = token.getOriginalText();
			}

			/*
	    newToken.append("<w l=\"").append(encodedToken)
		.append("\" la=\"").append(token.getLanguageCode());
	    String normalizedText =
		token.getOriginalText().replaceAll("\\\\","/");

	    if (previousWord != null) {
		newToken.append("\" pr=\"").append(previousWord);
	    }
	    if (previousWordStoplist == null ||
		    !previousWordStoplist.contains(normalizedText)) {
		previousWord = token.getOriginalText();
	    } else {
		// it's a stopword; leave the previous word as it is
	    }

	    if (token.hasContext()) {
		TokenContext context = token.getContext();
		int occurrence = context.getOccurrence();	    
		newToken.append(occurrence);
		newToken.append("\" n=\"").append(occurrence);
	    }

	    newToken.append("\" onclick=\"m(this,");

	    /*
	    if (documentsProcessed != -1) {
		newToken.append(",").append(documentsProcessed);
	    }
	    newToken.append("); return false\"");

	    if (token.getLanguageCode().equals(textLanguage)) {
		newToken.append(" class=\"text\"");
	    }
	    newToken.append("\">")
		.append(token.getDisplayText())
		.append("</w>");
			 */
			newToken.append("<a href=\"")
			.append(serviceURL)
			.append("?l=")
			.append(encodedToken)
			.append("&amp;la=")
			.append(token.getLanguageCode());

			// This is for prior probabilities
			if (previousWord != null) {
				newToken.append("&amp;prior=")
				.append(previousWord);
			}

			// This isn't foolproof, but it's quicker than parsing the
			// previous word again.
			String normalizedText =
				token.getOriginalText().replaceAll("\\\\","/");

			if (previousWordStoplist == null ||
					!previousWordStoplist.contains(normalizedText)) {
				previousWord = token.getOriginalText();
			} else {
				// it's a stopword; leave the previous word as it is
			}

			newToken.append("\" onclick=\"m(this,");

			if (token.hasContext()) {
				TokenContext context = token.getContext();
				int occurrence = context.getOccurrence();	    
				newToken.append(occurrence);
			} else {
				newToken.append("-1");
			}

			if (documentsProcessed != -1) {
				newToken.append(",").append(documentsProcessed);
			} else {
				newToken.append(",").append(0);
			}

			newToken.append("); return false\"");

			if (token.getLanguageCode().equals(textLanguage)) {
				newToken.append(" class=\"text\"");
			}

			if (opensInNewWindow) {
				newToken.append(" target=\"morph\"");
			}

			newToken.append(">")
			.append(token.getDisplayText())
			.append("</a>");

			token.setDisplayText(newToken.toString());
		}
	}

	public void setDocumentsProcessed(int dp) {
		documentsProcessed = dp;
	}


	public boolean willFilter(String text, String defaultLanguage) {
		// The MorphLinksTokenFilter wants to filter text if (a) it's in a
		// language with morph-data or (b) it contains text in a language with
		// morph-data, as represented by, say, <span class="greek">.
		if (LanguageCode.hasMorphData(defaultLanguage)) {
			return true;
		}

		return filterPattern.matcher(text).find();
	}
}
