import groovy.json.JsonBuilder;

class BerlinBrain{
    HashMap<String, BerlinGame> games = new HashMap<String, BerlinGame>();

    public addGame(BerlinGame game){
        String game_id = game.id
        games.put((game_id), game)
    }

    public updateGame(String game_id, def state){
        games.get((game_id)).updateState(state)
    }

    public releaseGame(String game_id){
        println "Here I will be removing a game"
    }

    public getMove(String game_id){
        BerlinGame game = games.get((game_id))
        List<Integer> nodes = game.map.myNodes
        List<HashMap<String, Integer>> moves = new ArrayList<HashMap<String, Integer>>();
        //Loop of all nodes to play in.
        for(node in nodes){
            BerlinNode berlinNode = game.map.nodes.get((node))
            
            if(berlinNode.number_of_soldiers==0){
                continue
            }
            List<Integer> adjascent = game.map.paths.get(berlinNode.node_id)

            //Are all neighboors allies?
            if(berlinNode.isSafe){
                
                //Move towards an unsafe node, with the highest unsafe ranking
                int warNodeID = adjascent.min {game.map.nodes.get(it).securityLevel}

                //Wait up, if our most dangerous candidate isn't gonna get attacked, zerg towards a plant we don't own
                if(game.map.nodes.get(warNodeID).number_of_soldiers == game.map.nodes.get(warNodeID).securityLevel){ 
                    warNodeID = zergRush( berlinNode.node_id, game.map)
                }

                moves.add(["from":node, "to":warNodeID, "number_of_soldiers":berlinNode.number_of_soldiers])
            } else {
                //Keep count of available soldiers after defense
                int availableSoldiers = berlinNode.number_of_soldiers
                if(berlinNode.node_type=="city"){ //Never surrender a city, fool
                    availableSoldiers = berlinNode.securityLevel
                }
                //Check all nodes that we can potentially win (we can look ahead another node later)
                List<Integer> warNodes = adjascent.findAll {
                    game.map.nodes.get(it).number_of_soldiers <= availableSoldiers && !game.map.nodes.get(it).isFriendly;
                }
                if(warNodes.size() > 0){
                    //Distribute forces 
                    List<HashMap<String, Integer>> tempMoves = new ArrayList<HashMap<String, Integer>>();
                    for(warNode in warNodes){
                        int soldiersToSend = game.map.nodes.get(warNode).number_of_soldiers + 1
                        if(availableSoldiers >= soldiersToSend){
                            availableSoldiers -= soldiersToSend
                            tempMoves.add(["from":node, "to":warNode, "number_of_soldiers":soldiersToSend])
                        }
                     }
                     //If we still have leftovers, split them

                    if(availableSoldiers >= warNodes.size()){
                        availableSoldiers = (int)(availableSoldiers / warNodes.size())
                        tempMoves.each {it.number_of_soldiers+=availableSoldiers}
                    }

                    moves.addAll(tempMoves)
                } else {
                   //Welp, looks like we're gonna get our ass kicked, better turtle!
                }
            }
        }

        def json = new JsonBuilder()
        json moves
        return json.toString()
    }

    private int zergRush(int startNode, BerlinMap map){
        int warNodeID=0

        //First, we find a city node_type==city && !isFriendly
        HashMap<Integer,BerlinNode> cities = map.nodes.findAll{!map.nodes.get(it.key).isFriendly && map.nodes.get(it.key).node_type=="city"};
        //Find the closest city
        int shortest = 9999
        def current

        for(city in cities){
            current = dijkstra(startNode, city.key, map.paths, map.nodes)

            if(current.distance < shortest){
                shortest = current.distance
                warNodeID = current.node
            }
        }

        return warNodeID
    }

    def dijkstra(int startNode, int goalNode, HashMap<Integer,List<Integer>> paths, HashMap<Integer,BerlinNode> nodes){
        def dist = [:]
        def previous = [:]
        for(node in nodes ){
           dist[node.key] = 9999
           previous[node.key] = null
        }
        dist[startNode] = 0;
        previous[startNode] = null;
        previous[goalNode] = startNode; //Case when we're next to a city
        

        HashMap<Integer,BerlinNode> nodesToProcess = new HashMap<Integer, BerlinNode>()
        nodesToProcess.putAll(nodes)
        while(nodesToProcess.size() != 0){
            def closestEntry = nodesToProcess.min{dist[it.key]}
            int closest = closestEntry.key
            if(dist[closest]==99){
                break; //No paths left
            }
            nodesToProcess.remove(closest)
            if(closest==goalNode){
                break;
            }
            paths.get(closest).each{
                def alt = dist[closest]+1
                if(alt < dist[it]){
                    dist[it] = alt
                    previous[it] = closest
                }    
            }
        }

        //Now extract the path
        int target = goalNode
        int distance = 0

        while(previous[target]!=startNode){
            target = previous[target]
            distance++
        }


        return ["distance":distance, "node":target];

    }

}
