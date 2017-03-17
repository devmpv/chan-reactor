'use strict'
let ToolbarConfig = {
  options: ['inline', 'fontSize', 'history'],
  inline: {
    inDropdown: false,
    className: undefined,
    options: ['bold', 'italic', 'underline', 'strikethrough', 'monospace', 'superscript', 'subscript'],
  },
  fontSize: {
    options: [10, 11, 12, 14],
    className: undefined,
    dropdownClassName: undefined,
  }
}
export default ToolbarConfig;
