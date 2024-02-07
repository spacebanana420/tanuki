package tanuki

import tanuki.tui.*
import tanuki.config.*

@main def main() =
  createConfig()
  if isConfigOk() then
    tui_title()
  else
    tui_configerror()

