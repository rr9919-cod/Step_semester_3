abstract class GardenTool {

    abstract String use();
}

class CuttingTool extends GardenTool {

    @Override
    String use() {
        return "Using the tool in the garden, blade sharpened first";
    }
}

class Pruner extends CuttingTool {

    @Override
    String use() {
        return super.use() +
               ", then trimming branches precisely";
    }
}

public class BackyardToolshed {

    public static void main(String[] args) {

        CuttingTool c = new CuttingTool();
        Pruner p = new Pruner();

        System.out.println(c.use());
        System.out.println(p.use());
    }
}
