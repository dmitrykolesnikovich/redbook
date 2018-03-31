/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2007 Erwin Coumans  http://continuousphysics.com/Bullet/
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose, 
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package javabullet.collision.narrowphase;

import javabullet.linearmath.IDebugDraw;
import javabullet.linearmath.Transform;

import javax.vecmath.Vector3f;

/**
 * ConvexCast is an interface for Casting.
 *
 * @author jezek2
 */
public interface ConvexCast {

    /**
     * Cast a convex against another convex object.
     */
    public boolean calcTimeOfImpact(Transform fromA, Transform toA, Transform fromB, Transform toB, CastResult result);

    ////////////////////////////////////////////////////////////////////////////

    /**
     * RayResult stores the closest result
     * alternatively, add a callback method to decide about closest/all results
     */
    public static class CastResult {
        public final Vector3f normal = new Vector3f();
        public final Vector3f hitPoint = new Vector3f();
        public final Transform hitTransformA = new Transform();
        public final Transform hitTransformB = new Transform();
        public float fraction = 1e30f;
        public IDebugDraw debugDrawer;

        public void debugDraw(float fraction) {
        }

        public void drawCoordSystem(Transform trans) {
        }
    }

}
