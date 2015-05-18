package pdstore;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents a binary blob in memory. This is a proxy class, i.e. the actual
 * blob data can be loaded on demand from the log file.
 * 
 * NOTE: the current lazy-loading implementation can set the log file and log
 * position properties of a blob only once it is added to the log. Hence, it is
 * important in the LogAndIndexStore that instances are written to the log first
 * and then to the index, so that these properties can be filled in when the
 * blob is written to the log and then be used when it is written to the index.
 * 
 * @author christof
 * 
 */
public class Blob implements Serializable, Comparable<Blob> {
	private static final long serialVersionUID = -525731204528928408L;

	byte[] data;

	// references for lazy loading of Blob data
	String fileName;
	long position;
	long length;

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public long getLength() {
		return length;
	}

	public byte[] getData() {
		if (data == null) {
			data = new byte[(int) length];
			try {
				RandomAccessFile file = new RandomAccessFile(
						new File(fileName), "r");
				file.seek(position);
				file.read(data);
				file.close();
			} catch (IOException e) {
				throw new PDStoreException("Error while lazily loading blob.",
						e);
			}
		}
		return data;
	}

	public Blob(String fileName, long position, long length) {
		this.data = null;
		this.fileName = fileName;
		this.position = position;
		this.length = length;
	}

	public Blob(byte[] data) {
		this.data = data;
		this.length = data.length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(getData());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Blob other = (Blob) obj;
		if (length != other.getLength())
			return false;
		if (!Arrays.equals(getData(), other.getData()))
			return false;
		return true;
	}

	@Override
	public int compareTo(Blob o) {
		// a longer blob is greater than a shorter blob
		// this doesn't require loading of blob data yet
		if (length > o.getLength())
			return 1;
		if (length > o.getLength())
			return -1;

		// otherwise use byte-wise comparison of the blob data
		byte[] data = getData();
		byte[] otherData = o.getData();
		for (int i = 0; i < length; i++) {
			if (data[i] != otherData[i])
				return data[i] - otherData[i];
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		byte[] data = getData();
		for (int i = 0; i < length; i++)
			s.append("" + data[i]);
		return s.toString();
	}
}
