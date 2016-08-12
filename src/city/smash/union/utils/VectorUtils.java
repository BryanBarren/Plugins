package city.smash.union.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import city.smash.union.Main;

public final class VectorUtils
{
	/**
	 * Rotates vector around the X axis with angle
	 * @param v vector to rotate
	 * @param angle angle to rotate by
	 */
	public static final Vector rotateAroundAxisX(Vector v, double angle)
	{
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double y = v.getY() * cos - v.getZ() * sin;
		double z = v.getY() * sin + v.getZ() * cos;
		return v.setY(y).setZ(z);
	}

	/**
	 * Rotates vector around the Y axis with angle
	 * @param v vector to rotate
	 * @param angle angle to rotate by
	 */
	public static final Vector rotateAroundAxisY(Vector v, double angle)
	{
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	/**
	 * Rotates vector around the Z axis with angle
	 * @param v vector to rotate
	 * @param angle angle to rotate by
	 */
	public static final Vector rotateAroundAxisZ(Vector v, double angle)
	{
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double x = v.getX() * cos - v.getY() * sin;
		double y = v.getX() * sin + v.getY() * cos;
		return v.setX(x).setY(y);
	}

	/**
	 * Rotates vector into the "view" of player/direction using yaw and pitch
	 * @param v vector to rotate
	 * @param pitch Pitch of "view"
	 * @param yaw Yaw of "view"
	 */
	public static final Vector rotateVectorIntoView(Vector v, double pitch, double yaw)
	{
		pitch = (pitch + 90F) * 0.01745329F;
		yaw = -yaw * 0.01745329F;
		
		rotateAroundAxisX(v, pitch);
		rotateAroundAxisY(v, yaw);
		return v;
	}
	/**
	 * Converts vector into pitch
	 * @param v Vector to check
	 * @return Pitch of vector
	 */
	public static final Float vectorToPitch(Vector v) {
		double distance = v.length();
		double pitch = Math.toDegrees(Math.asin(v.getY()/distance)) * -1D;
		
		return (float) pitch;
	}
	/**
	 * Converts vector into yaw
	 * @param v Vector to check
	 * @return Yaw of vector
	 */
	public static final Float vectorToYaw(Vector v) {
		double yaw = Math.toDegrees(Math.atan2(v.getX(), v.getZ()) * -1D);
		
		return (float) yaw;
	}

	/**
	 * Converts a vector to a location, where the 3 components represent coordinates
	 * @param world World of location
	 * @param v Vector to convert
	 */
	public static Location vectorToLoc(World world, Vector v) {
		Location loc = new Location(world, v.getX(), v.getY(), v.getZ());
		return loc;
	}

	public static Vector getRandomVector()
	{
		double x = Main.random.nextDouble() * 2D - 1D;
		double y = Main.random.nextDouble() * 2D - 1D;
		double z = Main.random.nextDouble() * 2D - 1D;
	
		return new Vector(x, y, z).normalize();
	}

	public static Vector getRandomCircleVector()
	{
		double rnd = Main.random.nextDouble() * 2D * Math.PI;
		double x = Math.cos(rnd);
		double z = Math.sin(rnd);
	
		return new Vector(x, 0D, z);
	}

	public static double getRandomAngle() {
		return Main.random.nextDouble() * 2D * Math.PI;
	}
}