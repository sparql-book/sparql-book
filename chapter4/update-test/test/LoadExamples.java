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
 * LOADのサンプル例
 *
 */
public class LoadExamples {
    public static void main(String[] args) {
        load1();
        load2();
        load3();
        try {
            load4();
        } catch (Exception e) {
            System.out.println("load4でエラーが発生しました");
        }
        load5();
        // load6();
    }

    /**
     * デフォルトグラフにロードする。
     */
    public static void load1() {
        System.out.println("##### load1 #####");
        GraphStore graphStore = createGraphStore();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("LOAD <file:/data/rdf/update-data1.ttl>", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 名前付きグラフにロードする。
     */
    public static void load2() {
        System.out.println("##### load2 #####");
        GraphStore graphStore = createGraphStore();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("LOAD <file:/data/rdf/update-data1.ttl> INTO GRAPH <http://sparqlbook.jp/graph1>",
                graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 複数回ロードする。
     */
    public static void load3() {
        System.out.println("##### load3 #####");
        GraphStore graphStore = createGraphStore();
        printDebug(graphStore, "before");
        String cmd = StrUtils.strjoin(" ;\n",
                "LOAD <file:/data/rdf/update-data1.ttl> INTO GRAPH <http://sparqlbook.jp/graph>",
                "LOAD <file:/data/rdf/update-data2.ttl> INTO GRAPH <http://sparqlbook.jp/graph>");
        UpdateAction.parseExecute(cmd, graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 存在しないファイルを指定してロードする。 エラーが発生する。
     */
    public static void load4() {
        System.out.println("##### load4 #####");
        GraphStore graphStore = createGraphStore();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("LOAD <http://sparqlbook.jp/not_exist.ttl> INTO GRAPH <http://sparqlbook.jp/graph>",
                graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * 存在しないファイルを指定してロードする。 SILENTによりエラーを発生させないようにする。
     */
    public static void load5() {
        System.out.println("##### load5 #####");
        GraphStore graphStore = createGraphStore();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute(
                "LOAD SILENT <http://sparqlbook.jp/not_exist.ttl> INTO GRAPH <http://sparqlbook.jp/graph>", graphStore);
        printDebug(graphStore, "after");
    }

    /**
     * Web上のRDFをロードする。 ただし、このファイルは形式エラーで失敗する。
     */
    public static void load6() {
        System.out.println("##### load6 #####");
        GraphStore graphStore = createGraphStore();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("LOAD <http://dbpedia.org/data/London.n3>", graphStore);
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
