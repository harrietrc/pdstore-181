package pdstore.sparql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.ChangeTemplateKind;
import pdstore.generic.GenericPDStore;
import pdstore.sparql.Query.OptimimzerType;

public class QueryOptimizerLSO extends QueryOptimizerBase {

	GenericPDStore<GUID, Object, GUID> statStore = null;

	@Override
	public long costInWhereClause(Query q, Set<Variable> assignedVariables,
			PDChange<GUID, Object, GUID> currentpattern) {
		statStore = (PDStore) q.store;

		long currentChangeCost = -1;

		// current change is given such a high cost before hand, because if
		// the
		// statistics does not have the selectivity for this change, then
		// its considered very low selective possible.
		currentChangeCost = -1;
		ChangeTemplateKind templateKind = ChangeTemplateKind.getKind(
				currentpattern, assignedVariables);

		if (templateKind == ChangeTemplateKind.IRX
				|| templateKind == ChangeTemplateKind.XRI) {
			// Do the next step, only if IRX or XRI has constants on their
			// instance position
			if (isIRXorXRI_withIasConstant(currentpattern)) {
				// todo: can this be a part of stats ??
				if (IRX_XRI_C.containsKey(currentpattern)) {

					currentChangeCost = IRX_XRI_C.get(currentpattern);

				} else {
					Collection<PDChange<GUID, Object, GUID>> changes = statStore
							.getChanges(currentpattern);
					if (!changes.isEmpty())
						currentChangeCost = changes.toArray().length;
					IRX_XRI_C.put(currentpattern, currentChangeCost);
				}
			}
		}

		if (currentChangeCost == -1) {
			currentChangeCost = estimateCostUsingLearningStatistics(
					currentpattern, templateKind);
		}

		if (currentChangeCost == -1) {
			// TODO: check with christof/Gerald
			QueryOptimizerRBO rbo = new QueryOptimizerRBO();
			return rbo.costInWhereClause(q, assignedVariables, currentpattern);
		}
		return currentChangeCost;
	}

	long estimateCostUsingLearningStatistics(PDChange<GUID, Object, GUID> w,
			ChangeTemplateKind templateKind) {
		long currentChangeCost = -1;
		GUID role2 = w.getRole2();
		Object tempcurrentChangeCost = null;
		// XRX
		if (ChangeTemplateKind.XRX == templateKind) {
			if (role_XRX.containsKey(role2)) {
				tempcurrentChangeCost = role_XRX.get(role2);
			} else {
				tempcurrentChangeCost = statStore.getInstance(null, role2,
						PDStore.ROLE_XRX_SELECTIVITY_ROLEID);
				if (tempcurrentChangeCost != null)
					role_XRX.put(role2, (Long) tempcurrentChangeCost);
			}
			// IRX
		} else if (ChangeTemplateKind.IRX == templateKind) {
			if (role_IRX.containsKey(role2)) {
				tempcurrentChangeCost = role_IRX.get(role2);
			} else {
				tempcurrentChangeCost = statStore.getInstance(null, role2,
						PDStore.ROLE_IRX_SELECTIVITY_ROLEID);
				if (tempcurrentChangeCost != null)
					role_IRX.put(role2, (Long) tempcurrentChangeCost);
			}
			// XRI
		} else if (ChangeTemplateKind.XRI == templateKind) {
			if (role_XRI.containsKey(role2)) {
				tempcurrentChangeCost = role_XRI.get(role2);
			} else {
				tempcurrentChangeCost = statStore.getInstance(null, role2,
						PDStore.ROLE_XRI_SELECTIVITY_ROLEID);
				if (tempcurrentChangeCost != null)
					role_XRI.put(role2, (Long) tempcurrentChangeCost);
			}
		}
		if (tempcurrentChangeCost != null)
			currentChangeCost = (Long) tempcurrentChangeCost;

		return currentChangeCost;
	}

}