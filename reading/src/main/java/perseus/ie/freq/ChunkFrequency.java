package perseus.ie.freq;

import perseus.document.Chunk;
import perseus.document.dao.HibernateChunkDAO;
import perseus.document.dao.HibernateChunkDAO.ChunkRow;

/**
 * Abstract class representing a frequency of an entity within a particular
 * chunk.
 */

public abstract class ChunkFrequency extends Frequency {
    private Chunk chunk;

    public ChunkRow getChunkRow() {
        return HibernateChunkDAO.chunkToRow(chunk);
    }

    public void setChunkRow(ChunkRow chunkRow) {
        chunk = chunkRow.toChunk();
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }
}
