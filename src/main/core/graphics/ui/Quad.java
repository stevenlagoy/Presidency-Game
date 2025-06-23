package main.core.graphics.ui;

import org.joml.Vector3f;

import main.core.graphics.entity.Entity;
import main.core.graphics.entity.Model;
import main.core.graphics.entity.QuadModel;

public class Quad extends Entity {

    public Quad(QuadModel model, Vector3f pos, Vector3f rotation, float scale) {
        super(model, pos, rotation, scale);
    }

    public Quad(QuadModel model, Vector3f pos) { 
        super(model, pos);
    }
}
