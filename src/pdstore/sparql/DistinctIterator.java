package pdstore.sparql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.StatefulIterator;
import pdstore.GUID;
import pdstore.PDChange;

/**
 * This is a generic iterator the performs distinct operation on the result set
 * 
 * @author Ganesh
 * 
 */
public class DistinctIterator<Element> extends StatefulIterator<Element> {

	private final Iterator<Element> baseIterator;
	HashSet<Element> resultSet = new HashSet<Element>();

	public DistinctIterator(
			Iterator<Element> baseIterator) {
		this.baseIterator = baseIterator;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Element computeNext() {
		while (baseIterator.hasNext()) {
			Element nextInBase = baseIterator.next();

			// if duplicate, then skip
			if (resultSet.contains(nextInBase))
				continue;

			resultSet.add(nextInBase);
			return (Element) nextInBase;
		}
		return null;
	}
}
