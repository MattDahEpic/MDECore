package com.mattdahepic.mdecore.asm.transformer;

import net.minecraft.block.material.MapColor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class TransformerMaterial extends CoreTransformer {
    public static final String TARGET_CLASS_NAME = "net.minecraft.block.material.Material";
    public static final String INVOKE_TARGET_CLASS_NAME = ;

    public TransformerMaterial () {
        mappings.put("circuits","field_151594_q");
    }
    @Override
    public String getApplicableClass() {
        return TARGET_CLASS_NAME;
    }
    @Override
    public byte[] transform(byte[] data) {
        ClassReader classReader = new ClassReader(data);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        //modify the circuit material
        for (FieldNode fieldNode : classNode.fields) {
            if (fieldNode.name.equals(getMappedName("circuits"))) {
                fieldNode.value = (new MaterialWaterproofCircuits(MapColor.airColor)).setNoPushMobility();
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
