package tanuki.runner

import scala.sys.process.*


def launch(cmd: List[String]) = cmd.run(ProcessLogger(line => ()))
