package com.zmide.lit.object;

public class BallData {
	private boolean Movable;
	private float firstLevel;
	private boolean canMove;
	private int mLastX;
	private int mLastY;
	
	//BallEnvironment 所需变量
	public boolean isMovable() {
		return Movable;
	}
	
	public void setMovable(boolean movable) {
		Movable = movable;
	}
	
	public float getFirstLevel() {
		return firstLevel;
	}
	
	public void setFirstLevel(float firstLevel) {
		this.firstLevel = firstLevel;
	}
	
	public boolean isCanMove() {
		return canMove;
	}
	
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	
	public int getLastX() {
		return mLastX;
	}
	
	public void setLastX(int mLastX) {
		this.mLastX = mLastX;
	}
	
	public int getLastY() {
		return mLastY;
	}
	
	public void setLastY(int mLastY) {
		this.mLastY = mLastY;
	}
}
