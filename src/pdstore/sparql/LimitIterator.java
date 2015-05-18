package pdstore.sparql;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import nz.ac.auckland.se.genoupe.tools.StatefulIterator;
import pdstore.GUID;

/**
 * This is a generic iterator that implements a filter.
 * 
 * @author Gerald
 * 
 */
public class LimitIterator<Element> extends StatefulIterator<Element> {

	private final Iterator<Element> baseIterator;
	long offsetCheckCount = 0;
	long limitCheckCount = 0;
	long limit;
	long offset;
	boolean isfirst = true;

	public LimitIterator(Iterator<Element> baseIterator, long limit, long offset) {
		this.baseIterator = baseIterator;
		this.limit = limit;
		this.offset = offset;
	}

	protected Element computeNext() {

		Element nextInBase = null;
		if (limit != -1 && limit == limitCheckCount)
			return null;

		while (baseIterator.hasNext()) {
			if (offsetCheckCount < offset) {
				offsetCheckCount++;
				baseIterator.next();
				continue;
			}
			limitCheckCount++;
			return (Element) baseIterator.next();
		}
		return null;
	}
}
