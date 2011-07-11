import groovy.json.*

//Intelligence is key, let's see what we have
def slurper = new JsonSlurper()
def infosExtract = request.getParameter('infos')
def mapExtract = request.getParameter('map')
def stateExtract = request.getParameter('state')
String action = request.getParameter('action')

BerlinGame games[];

if(infosExtract != null){
    def infos = slurper.parseText(infosExtract)
}
if(mapExtract != null){
    def map = slurper.parseText(mapExtract)
}
if(stateExtract != null){
    def state = slurper.parseText(stateExtract)
}

switch(action){
    case 'game_start':
        println "Game Start, en guarde!"
        //Register Game ID and set state
        games[infos.game_id]= new class BerlinGame(new BerlinMap(map))       
        games[infos.game_id].state = state
        break;
    case 'turn':
        println "My Turn, prepare to die!"
        //Update the state
        games[infos.game_id].state = state
        //Query brain to make a move
        getMove(games[infos.game_id])
        //Print Move
        break;
    case 'game_over':
        println "Good game!"
        //Derezz game
        games[infos.game_id]=""
        break;
    case 'ping':
        println "Ready to fight!"
        //Do nothing
        break;
    default:
        println "Invalid request. Sun Tzu is not pleased! >:["
}

def getMove(BerlinGame game){
    return 
}
