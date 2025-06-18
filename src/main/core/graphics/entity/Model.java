package main.core.graphics.entity;

public class Model {
    
    private int id;
    private int vertexCount;
    private int vboId;
    private int iboId;
    private Material material;

    public Model(int id, int vertexCount){
        this.id = id;
        this.vertexCount = vertexCount;
        this.vboId = -1;
        this.iboId = -1;
        this.material = new Material();
    }

    public Model(int id, int vertexCount, Texture texture) {
        this.id = id;
        this.vertexCount = vertexCount;
        this.material = new Material(texture);
        this.vboId = -1;
        this.iboId = -1;
    }

    public Model(Model model, Texture texture) {
        this.id = model.getId();
        this.vertexCount = model.getVertexCount();
        this.material = model.getMaterial();
        this.material.setTexture(texture);
        this.vboId = -1;
        this.iboId = -1;
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Texture getTexture() {
        return this.material.getTexture();
    }

    public void setTexture(Texture texture) {
        this.material.setTexture(texture);
    }

    public void setTexture(String textureName) {
        setTexture(TextureManager.getTexture(textureName));
    }

    public void setTexture(Texture texture, float reflectance) {
        this.material.setTexture(texture);
        this.material.setReflectance(reflectance);
    }

    public int getVertexBufferId() {
        return vboId;
    }

    public int getIndexBufferId() {
        return iboId;
    }

    public void setVertexBufferId(int id) {
        this.vboId = id;
    }

    public void setIndexBufferId(int id) {
        this.iboId = id;
    }
}
