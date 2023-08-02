import json

from elasticsearch7 import Elasticsearch, helpers
import os, glob
from nltk.stem import PorterStemmer

# reads the list of stopwords
with open('stoplist.txt') as f:
    stop_words = f.readlines()

# removes the \n from the lines
for i in range(len(stop_words)):
    stop_words[i] = stop_words[i][:-1]

config = {
    'host': '0.0.0.0'
}

cid = "_Group_23:dXMtZWFzdC0xLmF3cy5mb3VuZC5pbzo0NDMkZGE1M2M3Y2VlZWVmNGIzOTkzYmIyMmJmOWY4MzVhN2YkMDk1YWYyYjkzYzFlNGEyNmIzNWQyYjAzZjBjOWEzMDY="
es = Elasticsearch(request_timeout=10000, cloud_id=cid, http_auth=('elastic', 'm9xOFDipG8a2ZKuc1IdKluvK'))
#es = Elasticsearch(["http://localhost:9200"])

request_body = {
    "settings": {
        "number_of_shards": 1,
        "number_of_replicas": 1,
        "index.max_result_window": 100000,
        "analysis": {
            "filter": {
                "english_stop": {
                    "type": "stop",
                    "ignore_case": True,
                    "stopwords": stop_words
                }
            },
            "analyzer": {
                "stopped": {
                    "type": "custom",
                    "tokenizer": "standard",
                    "filter": [
                        "lowercase",
                        "english_stop",
                    ]
                }
            },
        }
    },
    "mappings": {
        "properties": {
            "content": {
                "type": "text",
                "fielddata": True,
                "analyzer": "stopped",
                "index_options": "positions"
            },
            "inlinks": {
                "type": "text"
            },
            "outlinks": {
                "type": "text"
            },
            "author": {
                "type": "text"
            }
        }
    }
}

response = es.indices.create(index="team23-merged", body=request_body)

# Reads outlinks and inlinks
def read_links():
    with open("outlinks.json", "r") as f:
        out = json.load(f)

    with open("inlinks.json", "r") as f:
        inl = json.load(f)

    return out, inl

outlinks, inlinks = read_links()

if os.name == 'posix':
    slash = "/"  # for Linux and macOS
else:
    slash = chr(92)  # '\' for Windows


def current_path():
    return os.path.dirname(os.path.realpath(__file__))


# default path is the script's current dir
def get_files_in_dir(self):
    # declare empty list for files
    file_list = []

    # iterate the files in dir using glob
    for filename in glob.glob(self):
        # add each file to the list
        file_list += [filename]

    # return the list of filenames
    return file_list


all_files = get_files_in_dir("./docs/*")


def yield_docs(files):
    ps = PorterStemmer()
    for _id, _file in enumerate(files):

        print(_file)
        content = read_file(_file)

        while '<DOC>' in content:
            docend = content.find('</DOC>')
            sub = content[:docend]
            docno_s = sub.find('<DOCNO>') + len('<DOCNO>')
            docno_e = sub.find('</DOCNO>')
            docno = sub[docno_s:docno_e].strip()
            text = ""

            while "<TEXT>" in sub:
                text_s = sub.find('<TEXT>') + len('<TEXT>')
                text_e = sub.find('</TEXT>')
                text = text + sub[text_s:text_e].strip() + "\n"
                sub = sub[text_e + len("</TEXT>"):]

            content = content[docend + len("</DOC>"):]
            doctext = text.lower()

            # removes punctuation
            replace = ['\n', ',', '.', '\'', '`', '"']
            for j in replace:
                doctext = doctext.replace(j, " ")

            # removes stopwords
            doclist = list(doctext.split(" "))
            nostops = []
            for j in doclist:
                if j not in stop_words:
                    nostops.append(j)

            # stems the words
            for j in range(len(nostops)):
                nostops[j] = ps.stem(nostops[j])

            final_text = " ".join(nostops)

            inl = []
            if docno in inlinks:
                inl = inlinks[docno]

            outl = []
            if docno in outlinks:
                outl = outlinks[docno]

            ret = {
                "text": final_text,
                "inlinks": inl,
                "outlinks": outl,
                "author": "Nicolas"
            }
            yield {
                "_index": 'team23-merged',
                "_id": docno,
                "_source": ret
            }


def read_file(filename):
    with open(filename, "r", encoding="ISO-8859-1", errors='ignore') as f:
        return f.read()

try:
    resp = helpers.bulk(
        es,
        yield_docs(all_files)
    )
    print("\nhelpers.bulk() RESPONSE:", resp)
    print("RESPONSE TYPE:", type(resp))
except Exception as err:
    print("\nhelpers.bulk() ERROR:", err)


