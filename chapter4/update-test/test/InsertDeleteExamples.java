/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.sparqlbook.update;

import org.apache.jena.atlas.lib.StrUtils;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sparql.sse.SSE;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;

/**
 * INSERT/DELETEのサンプル例
 *
 */
public class InsertDeleteExamples {

    public static void main(String[] args) {

        insert();
        delete();
        deleteWhere();
        deleteInsert();
        with();
        using();
    }

    /**
     * INSERTの実行に先立ち、データをロードする。
     * 
     * @return ロード後のGraphStore
     */
    public static GraphStore loadData() {
        GraphStore graphStore = createGraphStore();
        String cmd = StrUtils.strjoin(" ;\n",
                "LOAD <file:/data/rdf/update-data5.ttl> INTO GRAPH <http://sparqlbook.jp/graph1>");
        UpdateAction.parseExecute(cmd, graphStore);
        return graphStore;
    }

    /**
     * DELETE/INSERTの実行に先立ち、データをロードする。
     * 
     * @return ロード後のGraphStore
     */
    public static GraphStore loadData2() {
        GraphStore graphStore = createGraphStore();
        // load
        String cmd = StrUtils.strjoin(" ;\n",
                "LOAD <file:/data/rdf/update-data6.ttl> INTO GRAPH <http://sparqlbook.jp/graph1>",
                "LOAD <file:/data/rdf/update-data6.ttl> INTO GRAPH <http://sparqlbook.jp/graph2>");
        UpdateAction.parseExecute(cmd, graphStore);
        return graphStore;
    }

    /**
     * 既存の名前付きグラフから検索したデータを元に新しいデータを挿入する。
     */
    public static void insert() {
        System.out.println("##### insert #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                   + ""
                   + "INSERT {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    ?author rdf:type :Person ."
                   + "  }"
                   + "}"
                   + "WHERE {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    :book :author ?author . "
                   + "  }"
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 既存の名前付きグラフから検索したデータを元にデータを削除する。
     */
    public static void delete() {
        System.out.println("##### delete #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + ""
                   + "DELETE {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    ?author ?p ?o . "
                   + "  }"
                   + "}"
                   + "WHERE {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    :book :author ?author . "
                   + "    ?author ?p ?o . "
                   + "  }"
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 既存の名前付きグラフから検索したデータを元にデータを削除する。
     * DELETE直後にWHERE句を記述して、WHERE句で指定したパターンにマッチするデータを全て削除する。
     */
    public static void deleteWhere() {
        System.out.println("##### deleteWhere #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + ""
                   + "DELETE "
                   + "WHERE {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    :book :author ?author . "
                   + "    ?author ?p ?o . "
                   + "  }"
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 既存の名前付きグラフから検索したデータを元にデータを削除し、挿入する。
     */
    public static void deleteInsert() {
        System.out.println("##### deleteInsert #####");
        GraphStore graphStore = loadData2();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                   + "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>"
                   + ""
                   + "DELETE {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    ?author rdf:type :Person ."
                   + "  }"
                   + "}"
                   + "INSERT {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    ?author rdf:type dbpedia-owl:Person ."
                   + "  }"
                   + "}"
                   + "WHERE {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    :book :author ?author . "
                   + "  }"
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 既存の名前付きグラフから検索したデータを元にデータを削除し、挿入する。 WITH句を使用してグラフ指定を簡略化している。
     */
    public static void with() {
        System.out.println("##### with #####");
        GraphStore graphStore = loadData2();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                   + "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>"
                   + ""
                   + "WITH <http://sparqlbook.jp/graph1>"
                   + "DELETE {"
                   + "  ?author rdf:type :Person ."
                   + "}"
                   + "INSERT {"
                   + "  ?author rdf:type dbpedia-owl:Person ."
                   + "}"
                   + "WHERE {"
                   + "  :book :author ?author . "
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 既存の名前付きグラフから検索したデータを元にデータを削除し、挿入する。 USING句を使用してWHERE句の対象グラフ指定を指定している。
     */
    public static void using() {
        System.out.println("##### usnig #####");
        GraphStore graphStore = loadData2();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                   + "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>"
                   + ""
                   + "WITH <http://sparqlbook.jp/graph2>"
                   + "DELETE {"
                   + "  ?author rdf:type :Person ."
                   + "}"
                   + "INSERT {"
                   + "  ?author rdf:type dbpedia-owl:Person ."
                   + "}"
                   + "USING <http://sparqlbook.jp/graph1>"
                   + "WHERE {"
                   + "  :book :author ?author . "
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 空のGraphStore(TDB)のオブジェクトを生成して返す。
     * 
     * @return 空のGraphStoreオブジェクト
     */
    public static GraphStore createGraphStore() {
        Dataset ds = TDBFactory.createDataset();
        return GraphStoreFactory.create(ds);
    }

    /**
     * GraphStoreのデータの状態をデバッグ形式で出力する。
     * 
     * @param graphStore
     *            状態を出力したい GraphStore
     * @param title
     *            タイトル
     */
    public static void printDebug(GraphStore graphStore, String title) {
        System.out.println("##### " + title + " #####");
        SSE.write(graphStore);
        // System.out.println("# N-Quads: S P O G") ;
        // RDFDataMgr.write(System.out, graphStore, Lang.NQUADS) ;
    }
}
