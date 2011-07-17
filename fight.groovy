import groovy.json.*

//Intelligence is key, let's see what we have
def slurper = new JsonSlurper()
def infosExtract = request.getParameter('infos')
def mapExtract = request.getParameter('map')
def stateExtract = request.getParameter('state')
def infos
def map
def state
def action = request.getParameter('action')

BerlinBrain brain = new BerlinBrain();

//Extract using slurper
if(infosExtract != null){
    infos = slurper.parseText(infosExtract)
}
if(mapExtract != null){
    map = slurper.parseText(mapExtract)
}
if(stateExtract != null){
    state = slurper.parseText(stateExtract)
}

switch(action){
    case 'game_start':
        //Register Game ID and set state
        BerlinGame game = statelessCrutch(brain, infos, map, state);
        break;
    case 'turn':
        //Pass state to brain script
        BerlinGame game = statelessCrutch(brain, infos, map, state);
        //Query brain to make a move
        def move = brain.getMove(infos.game_id)
        //Print Move
        println move;
        break;
    case 'game_over':
        //Tell brain to discard the script
        //Useless in stateless println brain.releaseGame(infos.game_id)
        break;
    case 'ping':
        println "Ready to fight!"
        //Do nothing
        break;
    default:
        println "Invalid request. Sun Tzu is not pleased! >:["
}

BerlinGame statelessCrutch(brain, infos, map, state){
        BerlinGame game = new BerlinGame() 
        game.id = infos.game_id      
        BerlinMap berlinMap = new BerlinMap(isDirected:infos.directed, myID:infos.player_id)
        berlinMap.constructMap(map)
        game.map = berlinMap
        brain.addGame(game)
        brain.updateGame(infos.game_id, state)
        return game;
}
