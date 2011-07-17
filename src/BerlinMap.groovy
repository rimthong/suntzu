class BerlinMap{
    boolean isDirected = false;
    HashMap<Integer,BerlinNode> nodes = new HashMap<Integer,BerlinNode>();
    List<Integer> myNodes = new ArrayList<Integer>();
    HashMap<Integer,List<Integer>> paths = new HashMap<Integer, List<Integer>>();
    int myID;

    public constructMap(def map){
        //Construct Nodes
        for(node in map.nodes){
            nodes.put((node.id), new BerlinNode(node_id:node.id, node_type:node.type))
        }
        //Construct Paths
        for(path in map.paths){
            createPath(path.from, path.to)
            if(!isDirected){
                createPath(path.to, path.from)
            }
        }
    }

    //Helper method to link nodes
    public createPath(int from, int to){
        //Check if from exists
        if(paths.get((from))){
           paths.get((from)).add(to)
        } else {
           List<Integer> destinations = new ArrayList<Integer>();
           destinations.add(to)
           paths.put(from, destinations)
        }
    }

    //Sets the new state
    public String updateState(def state){
        myNodes=[];
        for(node in state){
            nodes.get(node.node_id).player_id=node.player_id;
            nodes.get(node.node_id).number_of_soldiers=node.number_of_soldiers;
            if(node.player_id == myID){
                nodes.get(node.node_id).isFriendly=true;
                myNodes.add(node.node_id)
            }
        }
        //Do a second pass in own nodes to compute security levels
        for(node in myNodes){
                computeSecurityLevel(node)
        }
        return this.toString();
    }

    public String toString(){
        String allNodes = "";
        for(node in nodes){
           allNodes += node.toString();
           allNodes += getPaths(node.key);

        }
        return "Map nodes: "+ allNodes;
    }

    public String getPaths(int from){
        String allPaths = "";
        for(destination in paths.get((from))){
            allPaths += " -> "+destination+"\n";
        }
        return allPaths;
    }


    //Computes how dangerous the place is, regarding to your number of soldiers vs surroundings
    public computeSecurityLevel(int nodeID){
        BerlinNode node = nodes.get(nodeID)
        List<Integer> invaders = paths.get(nodeID)
        Integer securityLevel = node.number_of_soldiers;
        int friendCount=0;
        for(node_id in invaders){
            if(nodes.get(node_id).player_id!=myID){
                securityLevel = securityLevel - nodes.get(node_id).number_of_soldiers;
            } else {
                //False Alarm
                friendCount++
            }
        }
        if(friendCount==invaders.size()){
            node.isSafe=true;
        }
        node.securityLevel=securityLevel;
    }
}
