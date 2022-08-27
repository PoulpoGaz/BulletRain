package fr.poulpogaz.jam.renderer.shaders;

import fr.poulpogaz.json.IJsonReader;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.JsonReader;
import fr.poulpogaz.jam.renderer.utils.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shaders {

    private static final Logger LOGGER = LogManager.getLogger(Shaders.class);

    public static Program COLOR;
    public static Program INST_COLOR;

    public static Program TEXTURE;
    public static Program INST_TEXTURE;

    public static Program COLOR_TEXTURE;
    public static Program INST_COLOR_TEXTURE;

    public static Program FONT;

    private static final Map<String, Shader> VERTEX = new HashMap<>();
    private static final Map<String, Shader> FRAGMENT = new HashMap<>();

    private static final List<Program> programs = new ArrayList<>();

    private static final HashMap<String, ShaderInfo> INFOS = new HashMap<>();

    public static void init() throws IOException {
        LOGGER.info("Loading shaders");
        loadInfos();

        COLOR = INFOS.get("color").createShader();
        INST_COLOR = INFOS.get("inst_color").createShader();
        TEXTURE = INFOS.get("texture").createShader();
        INST_TEXTURE = INFOS.get("inst_texture").createShader();
        COLOR_TEXTURE = INFOS.get("texture_color").createShader();
        INST_COLOR_TEXTURE = INFOS.get("inst_texture_color").createShader();
        FONT = INFOS.get("font").createShader();
    }

    public static void dispose() {
        programs.forEach(Program::dispose);
        VERTEX.values().forEach(Shader::dispose);
        FRAGMENT.values().forEach(Shader::dispose);

        VERTEX.clear();
        FRAGMENT.clear();
        programs.clear();
    }


    private static void loadInfos() throws IOException {
        try {
            IJsonReader jr = new JsonReader(Resource.asStream("shaders/shaders.json"));

            jr.beginObject();

            while (!jr.isObjectEnd()) {
                String key = jr.nextKey();

                jr.beginObject();
                ShaderInfo info = parseInfo(jr);

                if (info == null) {
                    LOGGER.warn("Failed to get info for shader {}", key);
                } else {
                    INFOS.put(key, info);
                }

                jr.endObject();
            }

            jr.endObject();
            jr.close();
        } catch (JsonException e) {
            throw new IOException(e);
        }
    }

    private static ShaderInfo parseInfo(IJsonReader jr) throws JsonException, IOException {
        ShaderInfoBuilder builder = new ShaderInfoBuilder();

        while (!jr.isObjectEnd()) {
            String key = jr.nextKey();

            switch (key) {
                case "vert" -> builder.setVert(jr.nextString());
                case "frag" -> builder.setFrag(jr.nextString());
                case "parent" -> builder.setParent(jr.nextString());
                case "attributes" -> builder.setAttributes(parseList(jr));
                case "uniforms" -> builder.setUniforms(parseList(jr));
                case "uniforms_to_attribs" -> builder.setUniformsToAttribs(parseList(jr));
            }
        }

        return builder.build();
    }

    private static List<String> parseList(IJsonReader jr) throws JsonException, IOException {
        List<String> list = new ArrayList<>();
        jr.beginArray();

        while (!jr.isArrayEnd()) {
            list.add(jr.nextString());
        }

        jr.endArray();
        return list;
    }

    private record ShaderInfo(String vert, String frag,
                              List<String> attributes,
                              List<String> uniforms) {

        public Program createShader() throws IOException {
            LOGGER.info("Creating program with vert={} and frag={}", vert(), frag());

            Shader vert = VERTEX.get(vert());
            if (vert == null) {
                vert = Shader.newShader(ShaderType.VERTEX, vert());
                VERTEX.put(vert(), vert);
            }

            Shader frag = FRAGMENT.get(frag());
            if (frag == null) {
                frag = Shader.newShader(ShaderType.FRAGMENT, frag());
                FRAGMENT.put(frag(), frag);
            }

            Program program = new Program(vert, frag);
            for (int i = 0; i < attributes.size(); i++) {
                program.bindAttribute(i, attributes.get(i));
            }

            program.link();

            for (String uniform : uniforms) {
                program.createUniform(uniform);
            }

            return program;
        }
    }

    private static class ShaderInfoBuilder {
        private String vert;
        private String frag;
        private List<String> attributes;
        private List<String> uniforms;
        private String parent;
        private List<String> uniformsToAttribs;

        public ShaderInfoBuilder() {
        }

        public ShaderInfo build() {
            if (parent != null) {
                ShaderInfo parentInfo = INFOS.get(parent);

                if (parentInfo == null) {
                    return null;
                }

                if (vert == null) {
                    vert = parentInfo.vert;
                }
                if (frag == null) {
                    frag = parentInfo.frag;
                }
                if (attributes == null) {
                    attributes = new ArrayList<>(parentInfo.attributes);
                }
                if (uniforms == null) {
                    uniforms = new ArrayList<>(parentInfo.uniforms);
                }

                if (uniformsToAttribs != null) {
                    attributes.addAll(uniformsToAttribs);
                    uniforms.removeAll(uniformsToAttribs);
                }
            }

            if (vert == null || frag == null || attributes == null || uniforms == null) {
                return null;
            }

            return new ShaderInfo(vert, frag, attributes, uniforms);
        }

        public String getVert() {
            return vert;
        }

        public ShaderInfoBuilder setVert(String vert) {
            this.vert = vert;
            return this;
        }

        public String getFrag() {
            return frag;
        }

        public ShaderInfoBuilder setFrag(String frag) {
            this.frag = frag;
            return this;
        }

        public List<String> getAttributes() {
            return attributes;
        }

        public ShaderInfoBuilder setAttributes(List<String> attributes) {
            this.attributes = attributes;
            return this;
        }

        public List<String> getUniforms() {
            return uniforms;
        }

        public ShaderInfoBuilder setUniforms(List<String> uniforms) {
            this.uniforms = uniforms;
            return this;
        }

        public String getParent() {
            return parent;
        }

        public ShaderInfoBuilder setParent(String parent) {
            this.parent = parent;
            return this;
        }

        public List<String> getUniformsToAttribs() {
            return uniformsToAttribs;
        }

        public ShaderInfoBuilder setUniformsToAttribs(List<String> uniformsToAttribs) {
            this.uniformsToAttribs = uniformsToAttribs;
            return this;
        }
    }
}
