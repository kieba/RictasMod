package rictas.energy;


public interface IWalls {
	public void enableWall(int side, boolean cutConnection);
	public void disableWall(int side);
	public WallLogic getWallLogic();
}
