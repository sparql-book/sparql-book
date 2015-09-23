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

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sparql.sse.SSE;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;

/**
 * CREATEのサンプル例
 *
 */
public class CreateExamples {

    public static void main(String[] args) {

        create1();
    }

    /**
     * 新しいグラフを作成する。 ただし、空のグラフを保持しないRDFストアの場合にはグラフは作成されない。
     */
    public static void create1() {
        System.out.println("##### create1 #####");
        GraphStore graphStore = createGraphStore();
        printDebug(graphStore, "before");
        UpdateAction.parseExecute("CREATE GRAPH <http://sparqlbook.jp/newgraph>", graphStore);
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
