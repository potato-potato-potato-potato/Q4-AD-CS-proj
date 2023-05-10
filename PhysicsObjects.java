public class PhysicsObjects<V> {

    private V object;
    private Vector velocity = new Vector(0, 0);

    public PhysicsObjects(V object) {
        this.object = object;
    }

}
