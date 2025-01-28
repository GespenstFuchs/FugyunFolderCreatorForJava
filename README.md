# FugyunFolderCreatorForJava
ふぎゅんフォルダ作成（Ｊａｖａ＋Ｓｗｉｎｇ版）

◆メモ
作業フォルダパス：C:\Users\fugyu\OneDrive\デスクトップ\TestJave
対象Jarファイルパス：C:\Users\fugyu\OneDrive\デスクトップ\TestJave\build\FugyunFolderCreator.jar
JREパス：C:\Users\fugyu\Documents\java\21\jmods
メインクラス：fugyunfoldercreator.FugyunFolderCreator
名称：ふぎゅんフォルダ作成
バージョン：1.0
アイコンパス：C:\Users\fugyu\OneDrive\デスクトップ\TestJave\Icon.ico
説明：説明
著作権：著作権
ベンダー：ベンダー
モジュールパス：

◆やり方
１．Eclipseで実行可能なJarファイルを出力する。
２．コマンドの【input】で指定するフォルダを作成する。
３．コマンドの【input】で指定するフォルダの中に、exeにしたいJarファイルを配置する。
４．アイコンを設定する場合、作業ディレクトリに配置する。（実際に作成したフォルダ構成の場合、【TestJava】フォルダ）
５．jdepsで使用している依存モジュールを抽出し、jlinkで、最小のJREを生成する。
６．５で作成した最小のJREを作業ディレクトリに配置する。（ここで作成したJRE（カスタムJRE）を、作成コマンドの【--runtime-image】に指定する。）
７．コマンドプロンプトを起動し、作業ディレクトリをカレントディレクトリにし、以下のようなコマンドを実行する。


◆作成コマンド（【main-class】は、パッケージ.メインメソッドを含むクラス）
・exe・コンソール表示
jpackage --input C:\Users\fugyu\Documents\TestJave\build --name FugyunFolderCreator --main-jar FugyunFolderCreator.jar --main-class fugyunfoldercreator.MainFrame --type app-image --runtime-image C:\Users\fugyu\Documents\TestJave\runtime --icon C:\Users\fugyu\Documents\TestJave\Icon.ico --app-version 1.0 --copyright "Fugyun" --vendor "Fugyun" --verbose --win-console --dest "C:\Users\fugyu\OneDrive\デスクトップ\TestJave\Dist"

・exe・コンソール未表示
jpackage --input C:\Users\fugyu\Documents\TestJave\build --name ふぎゅんフォルダ作成 --main-jar FugyunFolderCreator.jar --main-class fugyunfoldercreator.MainFrame --type app-image --runtime-image C:\Users\fugyu\Documents\TestJave\runtime --icon C:\Users\fugyu\Documents\TestJave\Icon.ico --app-version 1.0 --copyright "ふぎゅん" --vendor "ふぎゅん" --verbose --dest "C:\Users\fugyu\OneDrive\デスクトップ\TestJave\Dist"

・インストーラー
jpackage --input C:\Users\fugyu\Documents\TestJave\build --name ふぎゅんフォルダ作成 --main-jar FugyunFolderCreator.jar --main-class fugyunfoldercreator.MainFrame --type exe --runtime-image C:\Users\fugyu\Documents\TestJave\runtime --icon C:\Users\fugyu\Documents\TestJave\Icon.ico --app-version 1.0 --copyright "ふぎゅん" --vendor "ふぎゅん" --verbose --win-dir-chooser --win-shortcut-prompt --dest "C:\Users\fugyu\OneDrive\デスクトップ\TestJave\Dist"

なお、作成されたexeは、コマンドプロンプトのログの【インストーラのEXEを次に生成しています】に記載されている。
（通常は、作業ディレクトリに生成されている。）

なお、インストールしたアプリは、【C:\Program Files】の中に入っている。

◆jpackageのコマンド
https://docs.oracle.com/javase/jp/14/docs/specs/man/jpackage.html


◆Eclipseで実行可能なJarファイルの作成方法
１．メニューの【ファイル】→【エクスポート】を選択し、【エクスポート】ダイアログを表示する。
２．【エクスポート】ダイアログから、【Java】→【実行可能 JAR ファイル】を選択し、【次へ】ボタンを押下する。
３．【起動構成】・【エクスポート先】・【ライブラリー処理】を設定し、【完了】ボタンを押下する。
　　なお、【ライブラリー処理】は、【生成する JAR に必須ライブラリーを抽出(E)】を選択し、その他はデフォルトで良い。


◆jdepsで、依存モジュールを確認する。
１．コマンドプロンプトを起動し、カレントディレクトリを、対象Jarファイルが存在しているフォルダにする。
２．以下のコマンドを実行し、結果を確認する。（対象Jarファイル：ふぎゅんフォルダ作成.jar）
コマンド：jdeps --list-deps --ignore-missing-deps ふぎゅんフォルダ作成.jar
別パターン（結果がもう少し情報が追加されている。）：jdeps -s FugyunFolderCreator.jar
結果の例：java.base
　　　　　java.datatransfer
　　　　　java.desktop


◆jlinkにて、カスタムJREを生成する。
１．以下のコマンドを実行する。
　　jlink --module-path C:\Users\fugyu\Documents\java\21\jmods --add-modules java.base,java.datatransfer,java.desktop --output runtime
　　【--add-modules】は、jdepsで取得した結果を記載する。
　　【--output】は、カスタムJREを入れておくフォルダ名

　※上記コマンドで、カスタムJREで作成した後、作成したカスタムJREで、対象Jarファイルが動くか確認する。
　コマンド：C:\Users\fugyu\Documents\TestJave\runtime\bin\java.exe -jar ふぎゅんフォルダ作成.jar

　【やり方】の６に戻る。
