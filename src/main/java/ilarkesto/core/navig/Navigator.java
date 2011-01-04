package ilarkesto.core.navig;

import ilarkesto.base.Str;
import ilarkesto.core.logging.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Navigator {

	private static final Log log = Log.get(Navigator.class);

	private Page rootPage = new Page(null);
	private List<Page> pages = new LinkedList<Page>();
	private List<Plugin> plugins = new ArrayList<Plugin>();
	private NavigatorObserver observer;

	public Navigator() {
		goBackToRoot();
	}

	public void execute(Item item) {
		if (item == null) return;
		log.info("execute:", item);
		Plugin plugin = item.getPlugin();
		if (plugin == null) return;
		plugin.execute(this, item);
	}

	public void goBack() {
		if (pages.size() <= 1) return;
		pages.remove(0);
		firePageChanged();
	}

	public void goNext(Page page) {
		log.info("goNext:", page);
		for (Item item : page.getItems()) {
			log.debug(" -", item);
		}
		pages.add(0, page);
		firePageChanged();
	}

	public int getDepth() {
		return pages.size();
	}

	public Page getPage() {
		return pages.get(0);
	}

	public void goBackToRoot() {
		pages.clear();
		pages.add(rootPage);
		firePageChanged();
	}

	public Page getRootPage() {
		return rootPage;
	}

	public void setObserver(NavigatorObserver observer) {
		this.observer = observer;
	}

	private void firePageChanged() {
		if (observer != null) observer.onPageChanged(this);
	}

	public void addPlugin(Plugin plugin) {
		plugins.add(plugin);
		plugin.initialize(this);
		if (getPage() == rootPage) firePageChanged();
	}

	@Override
	public String toString() {
		return Str.toStringHelper(this, getDepth(), getPage());
	}

}