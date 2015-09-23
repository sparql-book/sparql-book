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
 * INSERT DATAのサンプル例
 *
 */
public class InsertDataExamples {

    public static void main(String[] args) {
        insertData1();
        insertData2();
        insertData3();
        try {
            insertData4();
        } catch (Exception e) {
            System.out.println("insertData4でエラーが発生しました");
        }
    }

    /**
     * INSERT DATAの実行に先立ち、データをロードする。
     * 
     * @return ロード後のGraphStore
     */
    public static GraphStore loadData() {
        GraphStore graphStore = createGraphStore();
        String cmd = StrUtils.strjoin(" ;\n",
                "LOAD <file:/data/rdf/update-data1.ttl> INTO GRAPH <http://sparqlbook.jp/graph1>",
                "LOAD <file:/data/rdf/update-data1.ttl>"); // default graph
        UpdateAction.parseExecute(cmd, graphStore);
        return graphStore;
    }

    /**
     * 既存の名前付きグラフにデータを挿入する。
     */
    public static void insertData1() {
        System.out.println("##### insertData1 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + "INSERT DATA {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    :book :author :yoko ."
                   + "    :book :author :shuichi ."
                   + "  }"
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 存在しない名前付きグラフにデータを挿入する。 新規グラフが生成されることが期待される。
     */
    public static void insertData2() {
        System.out.println("##### insertData2 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + "INSERT DATA {"
                   + "  GRAPH <http://sparqlbook.jp/new_graph> {"
                   + "    :book :author :yoko ."
                   + "    :book :author :shuichi ."
                   + "  }"
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * デフォルトグラフにデータを挿入する。
     */
    public static void insertData3() {
        System.out.println("##### insertData3 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + "INSERT DATA {"
                   + "  :book :author :yoko ."
                   + "  :book :author :shuichi ."
                   + "}";
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * INSERT DATAで変数を使用してエラーが発生する例。
     */
    public static void insertData4() {
        System.out.println("##### insertData4 #####");
        GraphStore graphStore = loadData();
        printDebug(graphStore, "before");
        String cmd = "PREFIX : <http://sparqlbook.jp/>"
                   + "INSERT DATA {"
                   + "  GRAPH <http://sparqlbook.jp/graph1> {"
                   + "    ?book :author :yoko ."
                   + "    ?book :author :shuichi ."
                   + "  }"
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
