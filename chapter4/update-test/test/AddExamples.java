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
 * ADDの例
 *
 */
public class AddExamples {

    public static void main(String[] args) {
        add1();
        add2();
        add3();
        add4();
        try {
            add5();
        } catch (Exception e) {
            System.out.println("add5でエラーが発生しました");
        }
        add6();
        add7();
    }

    /**
     * ADDの実行に先立ち、データをロードする。
     * 
     * @return ロード後のGraphStore
     */
    public static GraphStore loadData() {
        GraphStore graphStore = createGraphStore();
        String cmd = StrUtils.strjoin(" ;\n",
                "LOAD <file:/data/rdf/update-data1.ttl> INTO GRAPH <http://sparqlbook.jp/graph1>",
                "LOAD <file:/data/rdf/update-data2.ttl> INTO GRAPH <http://sparqlbook.jp/graph2>",
                "LOAD <file:/data/rdf/update-data3.ttl>"); // default graph
        UpdateAction.parseExecute(cmd, graphStore);
        return graphStore;
    }

    /**
     * 既存の名前付きグラフのデータを別の既存の名前付きグラフに追加する。
     */
    public static void add1() {
        System.out.println("##### add1 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("ADD <http://sparqlbook.jp/graph1> TO <http://sparqlbook.jp/graph2>", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 既存の名前付きグラフのデータを別の既存の名前付きグラフに追加する。 GRAPHキーワードを明示。
     */
    public static void add2() {
        System.out.println("##### add2 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("ADD GRAPH <http://sparqlbook.jp/graph1> TO GRAPH <http://sparqlbook.jp/graph2>",
                graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 既存の名前付きグラフのデータを存在しない名前付きグラフに追加する。 新規グラフが生成されることが期待される。
     */
    public static void add3() {
        System.out.println("##### add3 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("ADD <http://sparqlbook.jp/graph1> TO <http://sparqlbook.jp/new_graph>", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * デフォルトグラフのデータを既存の名前付きグラフに追加する。
     */
    public static void add4() {
        System.out.println("##### add4 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("ADD DEFAULT TO GRAPH <http://sparqlbook.jp/graph2>", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 存在しない名前付きグラフのデータを別の既存の名前付きグラフに追加する。エラーが発生する。
     */
    public static void add5() {
        System.out.println("##### add5 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("ADD <http://sparqlbook.jp/no_exist_graph> TO <http://sparqlbook.jp/graph2>",
                graphStore);// 存在しないグラフ名
        printDebug(graphStore, "after");
    }

    /**
     * 存在しない名前付きグラフのデータを別の既存の名前付きグラフに追加する。SILENTによりエラーを発生させないようにする。
     */
    public static void add6() {
        System.out.println("##### add6 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("ADD SILENT <http://sparqlbook.jp/no_exist_graph> TO <http://sparqlbook.jp/graph2>",
                graphStore);// 存在しないグラフ名
        printDebug(graphStore, "after");
    }

    /**
     * 既存の名前付きグラフのデータを別の既存の名前付きグラフに追加する(add1)と同等のINSERT文。
     */
    public static void add7() {
        System.out.println("##### add7 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "INSERT {"
                   + "  GRAPH <http://sparqlbook.jp/graph2> { ?s ?p ?o }"
                   + "}"
                   + "WHERE {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> { ?s ?p ?o }"
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 新しいTDBのGraphStoreオブジェクトを生成して返す。
     * 
     * @return GraphStore 新しいGraphStoreオブジェクト
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
