package perseus.document.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Metadata;
import perseus.document.Query;
import perseus.document.TableOfContents;
import perseus.document.dao.HibernateChunkDAO.ChunkRow;
import perseus.language.Language;
import perseus.util.HibernateDAO;
import perseus.util.HibernateUtil;
import perseus.util.ResultsIterator;
import perseus.util.StringUtil;

public class HibernateTableOfContentsDAO extends HibernateDAO<TableOfContents> implements TableOfContentsDAO {

	private class ChunkIterator extends ResultsIterator<Chunk> {
		private org.hibernate.Query query;
		private TableOfContents toc;

		public ChunkIterator(TableOfContents toc, org.hibernate.Query query) {
			super(2000);
			this.query = query;
			this.toc = toc;
		}

		@Override
		protected Collection<Chunk> getResults(int firstResult, int maxResults) {
			query.setFirstResult(firstResult).setMaxResults(maxResults);

			List<Object[]> results = query.list();
			List<Chunk> chunks = new ArrayList<Chunk>(results.size());

			Chunk tocChunk = null;
			if (toc.getQuery().isSubDocument()) {
				tocChunk = new HibernateChunkDAO().getByQuery(toc.getQuery());
			}

			for (Object[] resultRow : results) {
				ChunkRow row = (ChunkRow) resultRow[0];
				String subquery = (String) resultRow[1];

				Chunk chunk = row.toChunk();

				/*This is supposed to filter out chunks that should appear in
		  the TOC for navigational purposes but aren't actually part
		  of this particular subdocument, and so shouldn't be
		  processed in whatever CorpusProcessor we're running*/
				if (tocChunk != null &&
						!(tocChunk.contains(chunk) || tocChunk.equals(chunk))) {

					continue;
				}

				chunk.setQuery(new Query(chunk.getDocumentID(), subquery));

				chunks.add(chunk);
			}

			return chunks;
		}
	}

	public void save(TableOfContents toc) {
		for (TableOfContents.ChunkNode node : toc.getChunks()) {
			if (node.getChunk().getId() == null) {
				getSession().save(node.getChunk());
			}
		}
		getSession().save(toc);
	}

	public void delete(TableOfContents toc) {
		getSession().delete(toc);
	}

	public void update(TableOfContents toc) {
		getSession().update(toc);
	}

	public void cleanup() {
		HibernateUtil.closeSession();
	}

	public void deleteByDocument(String documentID) {
		List<TableOfContents> tocs = getByDocument(documentID);
		for (TableOfContents toc : tocs) {
			delete(toc);
		}
	}

	public List<TableOfContents> getByDocument(String documentID) {
		Criteria criteria = getSession().createCriteria(TableOfContents.class)
		.add(Restrictions.eq("query.documentID", documentID));

		List<TableOfContents> results = criteria.list();
		for (TableOfContents toc : results) {
			toc.getXML();
		}
		return results;
	}


	public TableOfContents getById(int id) {
		Object result =
			getSession().load(TableOfContents.class, new Integer(id));
		return (result != null) ? (TableOfContents) result : null;
	}

	public TableOfContents get(Chunk chunk) {
		Metadata metadata = chunk.getMetadata();
		return get(chunk, metadata.getChunkSchemes().getDefaultScheme());
	}

	public TableOfContents get(Chunk chunk, String scheme) {
		String documentID = chunk.getQuery().getDocumentID();
		String subquery = chunk.getQuery().getQuery();

		StringBuilder queryString = new StringBuilder()
		.append("from TableOfContents toc where ")
		.append("toc.query.documentID = :docID ")
		.append("and toc.scheme = :scheme and ")
		.append(chunk.getQuery().isJustDocumentID() ?
				"toc.query.query is null" : "toc.query.query = :subquery");

		org.hibernate.Query query = 
			getSession().createQuery(queryString.toString())
			.setString("docID", documentID)
			.setString("scheme", scheme)
			.setCacheable(true);
		if (!chunk.getQuery().isJustDocumentID()) {
			query.setString("subquery", subquery);
		}

		List<TableOfContents> results = query.list();
		if (results.isEmpty()) {
			return null;
		}
		TableOfContents toc = results.get(0);
		toc.getXML();
		return toc;
	}

	public Chunk getChunkByQuery(TableOfContents toc, Query targetQuery) 
	throws InvalidQueryException {

		StringBuilder hql = new StringBuilder()
		.append("select node.chunk from TableOfContents toc ")
		.append("join toc.chunks node where ")
		.append("node.subquery = :subquery and ");
		if (toc != null) {
			hql.append("toc = :toc ");
		} else {
			hql.append("toc.query.documentID = :docID");
		}

		org.hibernate.Query query = getSession().createQuery(hql.toString());
		query.setParameter("subquery", targetQuery.getQuery());
		if (toc != null) {
			query.setParameter("toc", toc);
		} else {
			query.setParameter("docID", targetQuery.getDocumentID());
		}

		List<ChunkRow> results = query.list();
		if (results.isEmpty()) throw new InvalidQueryException(targetQuery);

		// For arabic texts, make sure to match chunk on case.
		boolean matchCase=Language.forCode(targetQuery.getMetadata().get(Metadata.LANGUAGE_KEY)).getAdapter().matchCase();

		// For lexica, the language may be english while the subject (and chunking standard) may be arabic
		if (Language.forCode(targetQuery.getMetadata().get(Metadata.SUBJECT_LANGUAGE_KEY)) == Language.ARABIC) {
			matchCase=true;
		}

		Chunk chunk = results.get(0).toChunk();
		if (matchCase) {
			for (ChunkRow chunkrow : results) {
				chunk=chunkrow.toChunk();
				if (targetQuery.getQueryElements().get(targetQuery.getQueryElements().size()-1).toString().equals(
						chunk.getQuery().getQueryElements().get(chunk.getQuery().getQueryElements().size()-1).toString())) {
				}
			}
		}

		chunk.setQuery(targetQuery);
		return chunk;
	}

	public Chunk getFirstChunkOfType(TableOfContents contents, String type) {
		return getFirstChunkOfType(contents, type, null);
	}

	public Chunk getFirstChunkOfType(TableOfContents toc, String type, String value) {
		StringBuilder hql = new StringBuilder()
		.append("select node.chunk, node.subquery from TableOfContents toc ")
		.append("join toc.chunks node where node.chunk.type = :type ");
		if (value != null) { hql.append("and node.chunk.value = :value "); }
		hql.append("and toc = :toc ")
		.append("order by node.chunk.startOffset asc");

		org.hibernate.Query query = getSession().createQuery(hql.toString())
		.setParameter("type", type)
		.setParameter("toc", toc);

		if (value != null) { query.setParameter("value", value); }
		query.setMaxResults(1);

		List<Object[]> results = query.list();
		if (results.isEmpty()) return null;

		Object[] result = (Object[]) results.get(0);
		Chunk chunk = ((ChunkRow) result[0]).toChunk();
		String subquery = (String) result[1];
		chunk.setQuery(new Query(chunk.getDocumentID(), subquery));
		return chunk;
	}

	public Chunk getNextChunk(TableOfContents toc, Chunk chunk) {
		return getAdjacentChunk(toc, chunk, true);
	}

	public Chunk getPreviousChunk(TableOfContents toc, Chunk chunk) {
		return getAdjacentChunk(toc, chunk, false);
	}

	private Chunk getAdjacentChunk(TableOfContents toc, Chunk chunk, boolean next) {
		String operator = (next ? ">" : "<");
		String order = (next ? "asc" : "desc");

		org.hibernate.Query query = getSession().createQuery(
				"select node.chunk, node.subquery from TableOfContents toc " +
				"join toc.chunks node where " +
				"node.chunk.startOffset " + operator + " :offset and " +
				"node.chunk.type = :type and " +
				"toc = :toc order by node.chunk.startOffset " + order)
				.setParameter("offset", chunk.getStartOffset())
				.setParameter("type", chunk.getType())
				.setParameter("toc", toc)
				.setMaxResults(1);

		List<Object[]> results = query.list();
		if (results.isEmpty()) {
			return null;
		}

		Object[] result = results.get(0);
		ChunkRow row = (ChunkRow) result[0];
		String subquery = (String) result[1];

		Chunk resultChunk = row.toChunk();
		resultChunk.setQuery(new Query(chunk.getDocumentID(), subquery));
		return resultChunk;
	}

	public TableOfContents getTOCWithChunk(Chunk chunk) {
		return getTOCWithChunk(chunk, null);
	}

	public TableOfContents getTOCWithChunk(Chunk chunk, String scheme) {
		// If this is a subdocument, first see if we can find a TOC
		// directly matching this chunk
		if (chunk.getQuery().isSubDocument()) {
			String documentID = chunk.getQuery().getDocumentID();
			String subquery = chunk.getQuery().getQuery();

			org.hibernate.Query query = getSession().createQuery(
					"from TableOfContents toc where " +
					"toc.query.documentID = :documentID and " +
					"toc.query.query = :subquery " +
					((scheme != null) ? " and toc.scheme = :scheme" : ""))
					.setParameter("documentID", documentID)
					.setParameter("subquery", subquery);

			if (scheme != null) {
				query.setParameter("scheme", scheme);
			}

			List<TableOfContents> results = query.list();
			if (!results.isEmpty()) {
				return results.get(0);
			}
		}

		org.hibernate.Query query = getSession().createQuery(
				"from TableOfContents toc join toc.chunks node "+
				"where node.chunk = :chunk " +
				(scheme != null ? " and toc.scheme = :scheme" : ""))
				.setParameter("chunk", HibernateChunkDAO.chunkToRow(chunk));
		if (scheme != null) {
			query.setParameter("scheme", scheme);
		}

		// Prefer TOCs with subqueries over those without.
		List<TableOfContents> results = query.list();
		for (TableOfContents toc : results) {
			if (!toc.getQuery().isJustDocumentID()) {
				return toc;
			}
		}

		return (results.isEmpty() ? null : results.get(0));
	}

	public TableOfContents getTOCForQuery(Query initialQuery) {
		return getTOCForQuery(initialQuery, null);
	}

	public TableOfContents getTOCForQuery(Query initialQuery, String withType) {
		org.hibernate.Query query = getSession().createQuery(
				"from TableOfContents toc join toc.chunks node where toc.query.documentID = :docID" +
				(!initialQuery.isJustDocumentID() ? " and toc.query.query = :subquery" : "") +
				((withType != null) ? " and node.chunk.type = :type" : "") + " order by toc.class")
				.setParameter("docID", initialQuery.getDocumentID());

		if (!initialQuery.isJustDocumentID()) {
			query.setParameter("subquery", initialQuery.getQuery());
		}

		if (withType != null) {
			query.setParameter("type", withType);
		}

		List<TableOfContents> results = query.list();
		return (results.isEmpty() ? null : results.get(0));	
	}

	public ChunkIterator getAllChunks(TableOfContents toc, String type) {
		List<String> types = new ArrayList<String>();
		types.add(type);

		return getAllChunks(toc, types);
	}

	public ChunkIterator getAllChunks(TableOfContents toc, List<String> types) {
		boolean needTypes = (types != null && !types.isEmpty());

		org.hibernate.Query query = getSession().createQuery(
				"select node.chunk, node.subquery from TableOfContents toc " +
				"join toc.chunks node where toc = :toc" +
				(needTypes ?
						" and node.chunk.type in ('" +
						StringUtil.join(types, "','") + "')"
						: "") +
		" order by node.chunk.startOffset asc");
		query.setParameter("toc", toc);

		return new ChunkIterator(toc, query);
	}
}
