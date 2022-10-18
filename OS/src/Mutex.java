public class Mutex {

	boolean resource = true;
	Process hasResource = null;

	public void semWait(Process wantResource) {
		if (this.hasResource == wantResource || this.hasResource == null) {
			this.hasResource = wantResource;
			this.resource = false;
		} else {
			OS.scheduler.blockedQueue.add(wantResource);
		}
	}

	public void semSignal(Process hasResource, Process firstBlocked) {
		if (this.hasResource == hasResource) {
			if (firstBlocked != null) {
				OS.scheduler.blockedQueue.remove(firstBlocked);
				OS.scheduler.readyQueue.add(firstBlocked);
				this.hasResource = firstBlocked;
			} else {
				this.hasResource = null;
				this.resource = true;
			}
		}
	}

}
