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
 * CLEARのサンプル例
 *
 */
public class ClearExamples {

    public static void main(String[] args) {
        clear1();
        clear2();
        clear3();
        clear4();
        clear5();
        clear6();
    }

    /**
     * CLEARの実行に先立ち、データをロードする。
     * 
     * @return ロード後のGraphStore
     */
    public static GraphStore loadData() {
        GraphStore graphStore = createGraphStore();
        // load
        String cmd = StrUtils.strjoin(" ;\n",
                "LOAD <file:/data/rdf/update-data1.ttl> INTO GRAPH <http://sparqlbook.jp/graph1>",
                "LOAD <file:/data/rdf/update-data1.ttl> INTO GRAPH <http://sparqlbook.jp/graph2>",
                "LOAD <file:/data/rdf/update-data1.ttl>"); // default graph
        UpdateAction.parseExecute(cmd, graphStore);
        return graphStore;
    }

    /**
     * 指定した名前付きグラフをクリアする。
     */
    public static void clear1() {
        System.out.println("##### clear1 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("CLEAR GRAPH <http://sparqlbook.jp/graph1>", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * デフォルトグラフをクリアする。
     */
    public static void clear2() {
        System.out.println("##### clear2 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("CLEAR DEFAULT", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 全ての名前付きグラフをクリアする。
     */
    public static void clear3() {
        System.out.println("##### clear3 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("CLEAR NAMED", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 全てのグラフをクリアする。
     */
    public static void clear4() {
        System.out.println("##### clear4 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("CLEAR ALL", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 存在しないグラフを指定してクリアする。 エラーになるかはRDFストアの実装依存。
     */
    public static void clear5() {
        System.out.println("##### clear5 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("CLEAR GRAPH <http://sparqlbook.jp/not_exist_graph>", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 指定した名前付きグラフをクリアする(clear1)と同等のDELETE文。
     */
    public static void clear6() {
        System.out.println("##### clear6 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "DELETE {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> { ?s ?p ?o }"
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
