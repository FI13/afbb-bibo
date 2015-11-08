package de.afbb.bibo.ui.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.MediumType;

/**
 * content provider for {@link Copy} in a tree viewer.
 * 
 * @author dbecker
 */
public class CopyTreeContentProvider implements ITreeContentProvider {

	// map to store sets of copies grouped by their group id
	private final HashMap<Integer, Set<Copy>> groupedCopies = new HashMap<>();
	private final Set<Copy> input = new HashSet<>();
	private final HashMap<Integer, Copy> dummies = new HashMap<>();

	/**
	 * type for groups
	 */
	private static final MediumType groupType = new MediumType(-1, "Gruppe", IconType.MEDIA);

	@Override
	public void dispose() {
		groupedCopies.clear();
		input.clear();
		dummies.clear();
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		groupedCopies.clear();
		input.clear();
		if (newInput != null && newInput instanceof Collection<?>) {
			final Collection<? extends Copy> castInput = (Collection<? extends Copy>) newInput;
			// avoid allocation inside loop
			int groupId = -1;
			final Iterator<? extends Copy> iterator = castInput.iterator();
			while (iterator.hasNext()) {
				final Copy next = iterator.next();
				groupId = next.getGroupId();
				// add copy to grouped copies if it has a valid group id
				if (groupId > -1) {
					final Set<Copy> group = groupedCopies.containsKey(groupId) ? groupedCopies.get(groupId) : new HashSet<Copy>();
					group.add(next);
					groupedCopies.put(Integer.valueOf(groupId), group);
				} else {
					// add to normal input list when not belonging to group
					input.add(next);
				}
			}
		}
		// add dummy objects for each group, and add this dummy to regular input list
		final Iterator<Integer> iterator = groupedCopies.keySet().iterator();
		while (iterator.hasNext()) {
			final Integer next = iterator.next();
			final Copy dummy = new Copy();
			dummy.setGroupId(next);
			dummy.setType(groupType);
			dummies.put(next, dummy);
			input.add(dummy);
		}
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		return input.toArray();
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (hasChildren(parentElement)) {
			return groupedCopies.get(Integer.valueOf(((Copy) parentElement).getGroupId())).toArray();
		}
		return null;
	}

	@Override
	public Object getParent(final Object element) {
		if (element != null && element instanceof Copy) {
			final int groupId = ((Copy) element).getGroupId();
			if (groupId > -1) {
				return dummies.get(groupId);
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		return dummies.containsValue(element);
	}

}
