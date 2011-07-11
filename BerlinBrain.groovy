class BerlinBrain{
    HashMap<String, BerlinGame> games = new HashMap<String, BerlinGame>();

    public addGame(BerlinGame game){
        String game_id = game.id
        games.put((game_id), game)
        return "game ${game_id} added successfuly"
    }

    public updateGame(String game_id, def state){
        return games.get((game_id)).updateState(state)
    }

    public releaseGame(String game_id){
        println "Here I will be removing a game"
        return "released ${game_id} successfully"
    }

    public getMove(String game_id){
        String game = games.get((game_id))
        List<Integer> nodes = game.myNodes
        
        //Loop of all nodes to play in.
        for(node in nodes){
            BerlinNode berlinNode = game.map.nodes.get((node))
            
            //Are all neighboors allies?
                //Yes
                    //Move towards an unsafe node, with the highest unsafe ranking
                //No
                    //Is there a node that can be capped?
                        //Yes
                            //Will capturing de node make the current node safe?
                                //Yes, Attack with n
                                //No, Attack with n-(highest enemy node + 1)
                        //No, stand your ground

        }
        
        return "blah"
    }

}
