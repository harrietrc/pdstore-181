package pdstore.benchmark

import pdstore.{GUID, PDStore}

/** Simple "add changes then read changes" benchmark creating a fully-connected bipartite graph
  * between two sets of nodes of size n.
  * Created by christof on 1/31/14.
  */
object Bipartite {
  def main(args: Array[String]) {
    val n = 1000
    val nodes1 = List.fill[GUID](n)(new GUID)
    val nodes2 = List.fill[GUID](n)(new GUID)
    val role = new GUID
    val store = new PDStore("Bipartite-" + System.nanoTime)
    val t0 = System.nanoTime

    // add Cartesian product nodes1 x nodes2
    for (n1 <- nodes1; n2 <- nodes2)
      store.addLink(n1, role, n2)
    store.commit

    // get links
    for (n1 <- nodes1) store.getInstances(n1, role).size

    val t1 = System.nanoTime
    println(s"Bipartite($n): ${(t1 - t0) / 1000000000.0} seconds")
  }
}


