class BerlinGame{

    def map
    def id

    public String updateState(def newState){
        return map.updateState(newState)
    }

    public printState(){
        for (node in state){
            println node
        }
    }

}
