//Just a container class
class BerlinNode{
    public int node_id
    def player_id
    def number_of_soldiers
    def node_type
    boolean isSafe = false;
    boolean isFriendly = false;
    public int securityLevel;

    public String toString(){
        return "node_id:${node_id} player_id:${player_id} number_of_soldiers: ${number_of_soldiers} node_type:${node_type} isSafee:${isSafe} isFriendly:${isFriendly} securityLevel:${securityLevel}"
    }
}
